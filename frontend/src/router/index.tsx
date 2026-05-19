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
import {DriverLayout} from "../layout/DriverLayout.tsx";
import {DriverDashboard} from "../pages/DriverDashboard.tsx";

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
            // DRIVER
            {
                element: (
                    <RoleGuard
                        allowedRoles={["DRIVER"]}
                    />
                ),

                children: [
                    {
                        element: <DriverLayout />,

                        children: [
                            {
                                path: "/driver",
                                element: <DriverDashboard />,
                            },
                        ],
                    },
                ],
            },
        ],
    },
]);