import {Navigate, Outlet} from "react-router-dom";
import {useAuth} from "../useAuth.ts";
import type {UserRole} from "../../../types/users/userRole.ts";

type Props = {
    allowedRoles: UserRole[];
};

export const RoleGuard = ({ allowedRoles }: Props) => {
    const {user, isLoading} = useAuth();

    if (isLoading) {
    return <div>Loading...</div>;
    }

    if (!user) {
        return <Navigate to="/" replace/>;
    }

    const userRole = user.role;
    if (!allowedRoles.includes(userRole)) {
        if (userRole === "DRIVER") {
            console.log("Redirecting to drivers page");
            return <Navigate to="/driver" replace/>;
        }
        console.log("Redirecting to home page");
        return <Navigate to="/" replace/>;
    }

    return <Outlet/>;
};