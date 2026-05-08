import {Navigate, Outlet} from "react-router-dom";
import {getAccessToken} from "../utils/token.ts";

export const ProtectedRoute = () => {
    const token = getAccessToken();
    return token ? <Outlet/> : <Navigate to="/login" replace/>
}