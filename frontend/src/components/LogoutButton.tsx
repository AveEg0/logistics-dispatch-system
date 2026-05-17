
import { useNavigate } from "react-router-dom";
import {logout} from "../api/auth.ts";
import {clearAccessToken} from "../utils/token.ts";

export const LogoutButton = () => {
    const navigate = useNavigate();

    const handleLogout = () => {
        try {
            void logout()
        } finally {
            clearAccessToken();
            navigate("/login");
        }
    };

    return <button onClick={handleLogout}>Logout</button>;
};