import axios, { AxiosInstance } from 'axios';

const baseURL = import.meta.env.VITE_BASE_URL;

const apiInstance: AxiosInstance = axios.create({
    baseURL,
});


const api = {
    fraudAlerts: {
        
        getAll: () => apiInstance.get('/api/alerts'),
       
    },
    transactions: {

    }
};


export { api };

export { baseURL };