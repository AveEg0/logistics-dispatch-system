import { createBrowserRouter } from "react-router-dom";
import { LoginPage } from "../pages/LoginPage";
import {ProtectedRoute} from "../routes/ProtectedRoute.tsx";
import { Dashboard } from "../pages/Dashboard.tsx";
import {MainLayout} from "../layout/MainLayout.tsx";
import {Orders} from "../pages/Orders.tsx";

export const router = createBrowserRouter([
    {
        path: "/login",
        element: <LoginPage />,
    },

    {
        element: <ProtectedRoute />,
        children: [
            {
                element: <MainLayout />,
                children: [
                    {
                        path: "/",
                        element: <Dashboard />
                    },
                    {
                        path: "/orders",
                        element: <Orders />
                    }
                ]
            },
        ],
    },
]);