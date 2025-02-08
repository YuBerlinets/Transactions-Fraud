import axios, { AxiosInstance } from 'axios';

const baseURL = import.meta.env.VITE_BASE_URL;

const apiInstance: AxiosInstance = axios.create({
    baseURL,
});


const api = {
    fraudAlerts: {
        getAllPagination: (page: number, size: number, sort: string) => apiInstance.get('/api/fraud-alerts', { params: { page, size, sort } }),
    },
    transactions: {

    }
};


export { api };

export { baseURL };