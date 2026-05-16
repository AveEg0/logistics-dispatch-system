import type { Order } from "../../api/ordersApi";
import {OrdersStatusBadge} from "./ui/OrdersStatusBadge";
import React from "react";
import type {Driver} from "../../api/driverApi.ts";
import {AssignDriverAction} from "./AssignDriverAction.tsx";

type Props = {
    orders: Order[];
    onSort: (field: string) => void;
    sort: { field: string; direction: "asc" | "desc" };
    drivers: Driver[];
    onAssigned: () => void;
};

const cell: React.CSSProperties = { padding: "12px", verticalAlign: "middle" };

export const OrdersTable = ({ orders, onSort, sort, drivers, onAssigned }: Props) => {

    const renderHeader = (label: string, field: string) => {
        const sortable = field !== "";

        return (
            <th
                onClick={() => {
                    if (sortable) {
                        onSort(field);
                    }
                }}
                style={{
                    padding: "12px",
                    textAlign: "center",
                    borderBottom: "2px solid #e5e7eb",
                    fontSize: "20px",
                    fontWeight: "bold",
                    color: "#000",
                    cursor: sortable ? "pointer" : "default",
                }}
            >
                {label}
                {sortable && sort.field === field && (
                    <span style={{
                        marginLeft: 6,
                        fontSize: "16px",
                        fontWeight: "bold",
                        color: "#0a48d5"
                    }}>
                    {sort.direction === "asc" ? "▲" : "▼"}
                </span>
                )}
            </th>
        );
    };

    return (
        <div style={{ overflowX: 'auto' }}>
        <table style={{
            width: '100%',
            borderCollapse: 'collapse',
            fontSize: '14px'}}>
            <thead style={{ backgroundColor: '#989855'}}>
            <tr>
                {renderHeader("ID", "id")}
                {renderHeader("Pickup", "pickupLocation")}
                {renderHeader("Delivery", "deliveryLocation")}
                {renderHeader("Status", "status")}
                {renderHeader("Driver", "driverName")}
                {renderHeader("Created", "createdAt")}
                {renderHeader("Actions", "")}
            </tr>
            </thead>

            <tbody>
            {orders.map((o) => (
                <tr key={o.id}
                style={{
                    borderBottom: "1px solid #eee",
                }}>
                    <td style={cell}>{o.id}</td>
                    <td style={cell}>{o.pickupLocation}</td>
                    <td style={cell}>{o.deliveryLocation}</td>

                    <td>
                        <OrdersStatusBadge status={o.status} />
                    </td>

                    <td style={cell}>{o.driverName ?? "Unassigned"}</td>
                    <td style={cell}>{new Date(o.createdAt).toLocaleString()}</td>
                    <td>
                        {o.status === "CREATED" && (
                            <AssignDriverAction orderId={o.id} drivers={drivers} onAssigned={onAssigned}/>
                        )}
                    </td>
                </tr>
            ))}
            </tbody>
        </table>
        </div>
    );
};