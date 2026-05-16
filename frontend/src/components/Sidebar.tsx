 import {Link} from 'react-router-dom';

export const Sidebar = () => {
    return (
    <div style={{ width: "220px", background: "#111", color: "#fff", padding: "20px" }}>
        <h3>LOGISTICS</h3>

        <nav style={{ display: "flex", flexDirection: "column", gap: "10px" }}>
            <Link to="/">Dashboard</Link>
            <Link to="/orders">Orders</Link>
            <Link to="/orders/create">Create Order</Link>
            <Link to="/users">Users</Link>
            <Link to="/drivers">Drivers</Link>
        </nav>
    </div>
    );
};
