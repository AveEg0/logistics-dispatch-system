import {LogoutButton} from "./LogoutButton.tsx";

export const Header = () => {
    return (
        <div style={{
            height: "60px",
            borderBottom: "1px solid #ddd",
            display: "flex",
            justifyContent: "flex-end",
            alignItems: "center",
            padding: "0 20px" }}>
            <LogoutButton />
        </div>
    );
};