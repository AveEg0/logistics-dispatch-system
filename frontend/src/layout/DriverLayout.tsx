import {Outlet} from "react-router-dom";
import {Header} from "../components/Header.tsx";

export const DriverLayout = () => {
    return (
        <div style={{ display: "flex",
        height: "100vh"}}>
            <aside style={{ width: "220px", background: "#111", color: "#fff", padding: "20px" }}>
                <h2>Driver Panel</h2>
            </aside>
            <div style={{ flex: 1, display: "flex", flexDirection: "column" }}>
                <Header />

                <main style={{ padding: "20px", flex: 1 }}>
                    <Outlet />
                </main>
            </div>
        </div>
    )
 }