import type {Order} from "../../api/ordersApi.ts";

type Props = {
    order: Order;
    onAccept: () => void;
    onReject: () => void;
    onComplete: () => void;
};

export const DriverOrderCard = ({ order, onAccept, onReject, onComplete }: Props) => {
    return (
        <div style={{ border: "1px solid #ddd", padding: "20px", maxWidth: "600px", borderRadius: "12px" }}>
            <h2>Current Order</h2>
            <p><strong>Pickup:</strong> {order.pickupLocation}</p>
            <p><strong>Delivery:</strong> {order.deliveryLocation}</p>
            <p><strong>Description:</strong> {order.description}</p>
            <p><strong>Status:</strong> {order.status}</p>

            {order.status === 'ASSIGNED' && (
                <div style={{ marginTop: "20px" , display: "flex", gap: "12px"}}>
                    <button onClick={onAccept}>Accept</button>
                    <button onClick={onReject}>Reject</button>
                </div>
            )}

            {order.status === 'IN_PROGRESS' && (
                <div style={{ marginTop: "20px" , display: "flex", gap: "12px", justifyContent: "center"}}>
                    <button onClick={onComplete}>Complete</button>
                </div>
            )}
        </div>
    );
};
