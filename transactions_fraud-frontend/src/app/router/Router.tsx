import { createBrowserRouter } from "react-router-dom";
import App from "../layout/App";
import HomePage from "../../features/HomePage";


const router = createBrowserRouter([
    {
        path: '/',
        element: <App />,
        children: [
            {
                path: '/',
                element: <HomePage />
            },
        ],
    },

    {
        path: '*',
        element: <div>404</div>,
    },
]);

export { router };
