import { useEffect, useState } from "react";
import { Client } from "@stomp/stompjs";
import SockJS from 'sockjs-client';

import { LineChart, Line, XAxis, YAxis, Tooltip, CartesianGrid, ResponsiveContainer } from "recharts";

import "../assets/style/index.css";


interface Transaction {
    transactionId: string;
    userId: string;
    timestamp: number;
    amount: number;
}

interface FraudAlert {
    transactionId: string;
    userId: string;
    timestamp: number;
    amount: number;
    reason: string;
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
}

interface GraphProps {
    title: string;
    data: TimeWindowStats[];
    color: string;
}
interface ListProps {
    title: string;
    data: Transaction[] | FraudAlert[];
}

export default function HomePage() {
    const [transactionsData, setTransactionsData] = useState<TimeWindowStats[]>([]);
    const [blockedData, setBlockedData] = useState<TimeWindowStats[]>([]);
    const [approvedData, setApprovedData] = useState<TimeWindowStats[]>([]);
    const [lastTransactions, setLastTransactions] = useState<Transaction[]>([]);
    const [fraudAlerts, setFraudAlerts] = useState<FraudAlert[]>([]);

    useEffect(() => {
        const socket = new SockJS('http://localhost:8080/ws-statistics');
        const stompClient = new Client({
            webSocketFactory: () => socket,
            reconnectDelay: 5000,
            onConnect: () => {
                stompClient.subscribe('/topic/statistics', (response) => {
                    const data: StatisticsDTO = JSON.parse(response.body);

                    const formatted = data.timeWindowStats.map(stat => ({
                        ...stat,
                        time: new Date(stat.windowStart).toLocaleTimeString()
                    }));
                    console.log(data);
                    setTransactionsData(formatted.map(({ windowStart, total, blocked, approved }) => ({ windowStart, total, blocked, approved })));
                    setBlockedData(formatted.map(({ windowStart, total, blocked, approved }) => ({ windowStart, total, blocked, approved })));
                    setApprovedData(formatted.map(({ windowStart, total, blocked, approved }) => ({ windowStart, total, blocked, approved })));


                    setLastTransactions(data.lastTransactions);
                    setFraudAlerts(data.lastFraudAlerts);
                });
            },
        });

        stompClient.activate();
        return () => {

            stompClient.deactivate();
        }
    }, []);



    return (
        <div className="container">
            <div className="graphs">
                <Graph title="Transactions per 10 min" data={transactionsData} color="#8884d8" />
                <Graph title="Blocked Transactions" data={blockedData} color="#ff4d4d" />
                <Graph title="Approved Transactions" data={approvedData} color="#4caf50" />
            </div>
            <div className="lists">
                <List title="Last Transactions" data={lastTransactions} />
                <List title="Fraud Alerts" data={fraudAlerts} />
            </div>
        </div>
    );
}

function Graph({ title, data, color }: GraphProps) {
    return (
        <div className="graph">
            <h2>{title}</h2>
            <ResponsiveContainer width="100%" height={200}>
                <LineChart data={data}>
                    <CartesianGrid strokeDasharray="3 3" />
                    <XAxis dataKey="time" />
                    <YAxis />
                    <Tooltip />
                    <Line type="monotone" dataKey="count" stroke={color} strokeWidth={2} />
                </LineChart>
            </ResponsiveContainer>
        </div>
    );
}

function List({ title, data }: ListProps) {
    const formatTime = (timestamp: number) =>
        new Date(timestamp).toLocaleTimeString();

    return (
        <div className="list">
            <h2>{title}</h2>
            <ul>
                {data.map((item, idx) => (
                    <li key={idx}>
                        <span className="id">{item.transactionId}</span>
                        <span className="user">User: {item.userId}</span>
                        <span className="time">{formatTime(item.timestamp)}</span>
                        <span className="amount">${item.amount.toFixed(2)}</span>
                    </li>
                ))}
            </ul>
        </div>
    );
}