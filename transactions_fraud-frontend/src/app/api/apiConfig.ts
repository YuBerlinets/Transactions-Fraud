import axios, { AxiosInstance } from 'axios';

const baseURL = import.meta.env.VITE_BASE_URL;

const apiInstance: AxiosInstance = axios.create({
    baseURL,
});


const api = {
    fraudAlerts: {
        
        getAll: () => apiInstance.get('/api/alerts'),
        getByPublicUrl: (publicUrl: string, viewed: boolean) => apiInstance.get(`/api/collections/p/${publicUrl}`, { params: { viewed } }),
        getCollectionDetails: (collectionId: string) => apiInstance.get(`/api/collections/${collectionId}`),
        regeneratePublicUrl: (collectionId: string) => apiInstance.post(`/api/collections/regenerate-public-url/${collectionId}`),
        deleteCollection: (collectionId: string) => apiInstance.delete(`/api/collections/${collectionId}`),
        extendCollection: (collectionId: string) => apiInstance.post(`/api/collections/extend/${collectionId}`),
        updateDetails: (collectionId: string, newDetails: { collectionName: string, description: string }) => apiInstance.patch(`/api/collections/${collectionId}`, newDetails),
        submitReport: (collectionId: string, topics: string[]) => apiInstance.post(`/api/collections/report/${collectionId}`, { reasons: topics }),
    },
    transactions: {

    }
};


export { api };

export { baseURL };