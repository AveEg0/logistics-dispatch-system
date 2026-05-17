import { createBrowserRouter } from "react-router-dom";
import { LoginPage } from "../pages/LoginPage";
import {AuthGuard} from "../api/auth/guards/AuthGuard.tsx";
import { Dashboard } from "../pages/Dashboard.tsx";
import {MainLayout} from "../layout/MainLayout.tsx";
import Orders from "../pages/Orders.tsx";
import {Users} from "../pages/Users.tsx";
import {Drivers} from "../pages/Drivers.tsx";
import {CreateOrderPage} from "../pages/CreateOrderPage.tsx";
import {RoleGuard} from "../api/auth/guards/RoleGuard.tsx";

export const router = createBrowserRouter([
    // PUBLIC
    {
        path: "/login",
        element: <LoginPage />,
    },

    // AUTHENTICATED
    {
        element: <AuthGuard />,
        children: [

            // ADMIN | DISPATCHER
            {
                element: (<RoleGuard allowedRoles={['ADMIN', 'DISPATCHER']} />),
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
                    },
                    {
                        path: "/orders/create",
                        element: <CreateOrderPage />
                    },
                    {
                        path: "/users",
                        element: <Users />
                    },
                    {
                        path: "/drivers",
                        element: <Drivers />
                    },
                ]
            },
        ],
    },


        ],
    },
]);