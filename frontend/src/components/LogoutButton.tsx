import { clearTokens } from "../utils/token";
import { useNavigate } from "react-router-dom";

export const LogoutButton = () => {
    const navigate = useNavigate();

    const handleLogout = () => {
        clearTokens();
        navigate("/login");
    };

    return <button onClick={handleLogout}>Logout</button>;
};