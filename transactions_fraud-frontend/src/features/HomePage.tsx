import { useEffect, useState } from "react";
import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";
import {
    LineChart,
    Line,
    XAxis,
    YAxis,
    Tooltip,
    CartesianGrid,
    ResponsiveContainer
} from "recharts";

import "../assets/style/index.css";
import ThemeToggle from "./components/ThemeToggle";
import { useNavigate } from "react-router-dom";
import { baseURL } from "../app/api/apiConfig";

interface Transaction {
    transactionId: string;
    userId: string;
    timestamp: string;
    amount: string;
}

interface FraudAlert {
    transactionId: string;
    userId: string;
    timestamp: string;
    amount: string;
    reason?: string;
}

interface StatisticsDTO {
    timeWindowStats: Array<{
        windowStart: number;
        total: number;
        blocked: number;
        approved: number;
    }>;
    lastTransactions: Transaction[];
    lastFraudAlerts: FraudAlert[];
}

interface TimeWindowStats {
    windowStart: number;
    total: number;
    blocked: number;
    approved: number;
    time: string;
}

interface GraphProps {
    title: string;
    data: TimeWindowStats[];
    dataKey: string;
    color: string;
}

interface ListProps {
    title: string;
    data: Transaction[] | FraudAlert[];
    isFraud?: boolean;
}

export default function HomePage() {
    const [timeWindowStats, setTimeWindowStats] = useState<TimeWindowStats[]>([]);
    const [lastTransactions, setLastTransactions] = useState<Transaction[]>([]);
    const [fraudAlerts, setFraudAlerts] = useState<FraudAlert[]>([]);

    const navigate = useNavigate();

    useEffect(() => {
        const socket = new SockJS(`${baseURL}/ws-statistics`);
        const stompClient = new Client({
            webSocketFactory: () => socket,
            reconnectDelay: 5000,
            onConnect: () => {
                stompClient.subscribe("/topic/statistics", (response) => {
                    const data: StatisticsDTO = JSON.parse(response.body);
                    console.log(data);
                    const formattedStats: TimeWindowStats[] = data.timeWindowStats.map((stat) => ({
                        ...stat,
                        time: new Date(stat.windowStart).toLocaleTimeString()
                    }));
                    setTimeWindowStats(formattedStats);
                    setLastTransactions(data.lastTransactions.sort((a, b) => {
                        return new Date(b.timestamp).getTime() - new Date(a.timestamp).getTime();
                    }));
                    setFraudAlerts(data.lastFraudAlerts.sort((a, b) => {
                        return new Date(b.timestamp).getTime() - new Date(a.timestamp).getTime();
                    }));
                });
            },
        });

        stompClient.activate();
        return () => {
            stompClient.deactivate();
        };
    }, []);


    return (
        <div className="container">
            <div className="actions">
                <h1>Real-time Transaction Monitoring</h1>
                <div className="actions_right_side">
                    <a
                        onClick={() => navigate("/fraud-alerts")}
                        className="fraud_alerts_link"
                    >
                        Fraud Alerts
                    </a>
                    <span>Theme</span>
                    <ThemeToggle />
                </div>
            </div>
            <div className="graphs">
                <Graph
                    title="Transactions per 3 min"
                    data={timeWindowStats}
                    dataKey="total"
                    color="#8884d8"
                />
                <Graph
                    title="Blocked Transactions"
                    data={timeWindowStats}
                    dataKey="blocked"
                    color="#ff4d4d"
                />
                <Graph
                    title="Approved Transactions"
                    data={timeWindowStats}
                    dataKey="approved"
                    color="#4caf50"
                />
            </div>
            <div className="lists">
                <List title="Last Transactions" data={lastTransactions} />
                <List title="Fraud Alerts" data={fraudAlerts} isFraud={true} />
            </div>
        </div>
    );
}

function Graph({ title, data, dataKey, color }: GraphProps) {
    const GraphTooltip = ({ active, payload, label }: any) => {
        if (active && payload && payload.length) {
            return (
                <div className="graph_tooltip">
                    <p className="label">{`Time: ${label}`}</p>
                    <p className="value">{`${dataKey}: ${payload[0].value}`}</p>
                </div>
            );
        }
        return null;
    };

    return (
        <div className="graph">
            <h2>{title}</h2>
            <ResponsiveContainer width="100%" height={200}>
                <LineChart data={data}>
                    <CartesianGrid strokeDasharray="3 3" />
                    <XAxis dataKey="time" />
                    <YAxis />
                    <Tooltip content={<GraphTooltip />} />
                    <Line type="monotone" dataKey={dataKey} stroke={color} strokeWidth={2} />
                </LineChart>
            </ResponsiveContainer>
        </div>
    );
}

function List({ title, data, isFraud }: ListProps) {
    const formatTime = (timestamp: string) =>
        new Date(timestamp).toLocaleTimeString();

    return (
        <div className="list">
            <h2>{title}</h2>
            <div className="list-container">
                <div className="header">
                    <span>TRX ID</span>
                    <span>User ID</span>
                    <span>Time</span>
                    <span>Amount</span>
                </div>
                <ul>
                    {data.map((item, idx) => (
                        <li key={idx} className="row">
                            <span>{item.transactionId}</span>
                            <span>{item.userId}</span>
                            <span className="time">{formatTime(item.timestamp)}</span>
                            <span className={`amount ${isFraud ? "fraud" : ""}`}>
                                ${Number.parseFloat(item.amount).toFixed(2)}
                            </span>
                        </li>
                    ))}
                </ul>
            </div>
        </div>
    );
}