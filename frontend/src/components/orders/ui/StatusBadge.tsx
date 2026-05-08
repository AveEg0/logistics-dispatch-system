import type {OrderStatus} from "../../../types/orders/orderStatus.ts";
import React from "react";

export const StatusBadge = ({ status }: { status: OrderStatus }) => {
    const styles: Record<OrderStatus, React.CSSProperties> = {
        CREATED: { background: "#eee", color: "#555" },
        ASSIGNED: { background: "#dbeafe", color: "#1d4ed8" },
        IN_PROGRESS: { background: "#fef3c7", color: "#d97706" },
        COMPLETED: { background: "#dcfce7", color: "#15803d" },
        CANCELLED: { background: "#fee2e2", color: "#dc2626" },
    };

    return (
        <span
            style={{
                ...styles[status],
                padding: "4px 8px",
                borderRadius: "999px",
                fontSize: "12px",
                fontWeight: 500,
            }}
        >
      {status}
    </span>
    );
};