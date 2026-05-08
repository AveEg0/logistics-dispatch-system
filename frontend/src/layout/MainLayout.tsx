import { Outlet } from "react-router-dom";
import { Sidebar } from "../components/Sidebar";
import { Header } from "../components/Header";

export const MainLayout = () => {
    return (
        <div style={{ display: "flex", height: "100vh" }}>
    <Sidebar />

    <div style={{ flex: 1, display: "flex", flexDirection: "column" }}>
    <Header />

    <main style={{ padding: "20px", flex: 1 }}>
    <Outlet />
    </main>
    </div>
    </div>
);
};