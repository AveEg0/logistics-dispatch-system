import {Navigate, Outlet} from "react-router-dom";
import {useAuth} from "../useAuth.ts";

export const AuthGuard = () => {
    const {user, isLoading} = useAuth();

    if (isLoading) {
        return <div>Loading...</div>;
    }

    if (!user) {
        return <Navigate to="/login" replace/>
    }

    return <Outlet/>;
}