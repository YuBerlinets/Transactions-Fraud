import { useEffect, useState } from "react";
import { api } from "../app/api/apiConfig";
import { Table, Pagination, Spin, Space } from "antd";
import { useNavigate } from "react-router-dom";
import { FaArrowCircleLeft } from "react-icons/fa";

export interface FraudAlert {
    id: string;
    userId: string;
    transactionId: string;
    amount: number;
    timestamp: string;
    location: string;
}

export interface PaginationResponse<T> {
    currentPage: number;
    totalElements: number;
    totalPages: number;
    content: T[];
}

export default function FraudAlertsPage() {
    const [fraudAlertsPagination, setFraudAlertsPagination] = useState<PaginationResponse<FraudAlert>>({
        currentPage: 0,
        totalElements: 0,
        totalPages: 0,
        content: [],
    });
    const [currentPage, setCurrentPage] = useState(0);
    const [pageSize, setPageSize] = useState(10);
    const [totalElements, setTotalElements] = useState(0);
    const [loading, setLoading] = useState(true);
    const SORTBY = "timestamp,desc";

    const navigate = useNavigate();

    useEffect(() => {
        const fetchFraudAlerts = async () => {
            try {
                const response = await api.fraudAlerts.getAllPagination(currentPage, pageSize, SORTBY);
                setFraudAlertsPagination(response.data);
                setTotalElements(response.data.totalElements);
            } catch (error) {
                console.error("Error fetching fraud alerts:", error);
            } finally {
                setLoading(false);
            }
        };

        fetchFraudAlerts();
    }, [currentPage, pageSize]);

    const columns = [
        {
            title: "ID",
            dataIndex: "id",
            key: "id",
            ellipsis: true,
        },
        {
            title: "Transaction ID",
            dataIndex: "transactionId",
            key: "transactionId",
            ellipsis: true,
        },
        {
            title: "Amount",
            dataIndex: "amount",
            key: "amount",
            className: "amount fraud",
            render: (value: number) => `$${value.toFixed(2)}`,
        },
        {
            title: "Timestamp",
            dataIndex: "timestamp",
            key: "timestamp",
            render: (text: string) => new Date(text).toLocaleString(),
        },
        {
            title: "Location",
            dataIndex: "location",
            key: "location",
            ellipsis: true,
        },
    ];

    return (
        <div className="container">
            <div className="alerts_upper">

                <div className="alerts_upper_title">

                    <FaArrowCircleLeft
                        className="return_button_icon"
                        onClick={() => navigate("/")}
                    />

                    <h1>Fraud Alerts</h1>
                </div>
                <Pagination
                    current={currentPage + 1}
                    total={totalElements}
                    pageSize={pageSize}
                    onChange={(page) => setCurrentPage(page - 1)}
                    onShowSizeChange={(current, size) => { setPageSize(size); setCurrentPage(current); }}
                    pageSizeOptions={["10", "20", "50"]}
                    hideOnSinglePage={true}
                />
            </div>

            {loading ? (
                <Space style={{ width: "100%", justifyContent: "center", margin: "20px 0" }}>
                    <Spin size="large" />
                </Space>
            ) : (
                <>
                    <Table
                        dataSource={fraudAlertsPagination.content}
                        columns={columns}
                        rowKey="id"
                        pagination={false}
                        scroll={{ x: true }}
                        className="fraud_table"
                    />

                </>
            )}
        </div>
    );
}
