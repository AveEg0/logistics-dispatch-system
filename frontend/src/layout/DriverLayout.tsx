import {logout} from "../api/auth.ts";
import {Outlet} from "react-router-dom";

export const DriverLayout = () => {
    return (
        <div style={{ display: "flex",
        height: "100vh"}}>
            <aside style={{ width: "220px", background: "#111", color: "#fff", padding: "20px" }}>
                <h2>Driver Panel</h2>
                <button onClick={logout}
                style={{marginTop: "20px"}}>Logout
                </button>
            </aside>
            <main style={{ flex: 1, padding: "24px" }}><Outlet/></main>
        </div>
    )
 }