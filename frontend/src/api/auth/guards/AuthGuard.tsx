import {Navigate, Outlet} from "react-router-dom";
import {getAccessToken} from "../../../utils/token.ts";
import {useAuth} from "../useAuth.ts";

export const AuthGuard = () => {
    const token = getAccessToken();
    const {user, isLoading} = useAuth();

    if (!token) {
        return <Navigate to="/login" replace/>
    }

    if (isLoading) {
        return <div>Loading...</div>;
    }

    if (!user) {
        return <Navigate to="/login" replace/>
    }

    return <Outlet/>;
}