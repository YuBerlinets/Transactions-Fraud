import { createBrowserRouter, Navigate } from "react-router-dom";
import App from "../layout/App";
import HomePage from "../../features/HomePage";
import FraudAlertsPage from "../../features/FraudAlertsPage";


const router = createBrowserRouter([
    {
        path: '/',
        element: <App />,
        children: [
            {
                path: '/',
                element: <HomePage />
            },
            {
                path: '/fraud-alerts',
                element: <FraudAlertsPage />
            }
        ],

    },

    {
        path: '*',
        element: <Navigate to="/" replace />,
    },
]);

export { router };
