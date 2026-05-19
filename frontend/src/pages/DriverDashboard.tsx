import { useEffect, useState} from "react";
import {acceptOrder, fetchMyCurrentOrder, type Order, rejectOrder} from "../api/ordersApi.ts";
import {DriverOrderCard} from "../components/drivers/DriverOrderCard.tsx";

export const DriverDashboard = () => {
    const [order, setOrder] = useState<Order | null>(null);
    const [loading, setLoading] = useState(true);
    const [refreshIndex, setRefreshIndex] = useState(0);
    const [error, setError] = useState<string>("");

    useEffect(() => {

        let cancelled = false;
        fetchMyCurrentOrder()
            .then((data) => {
                if (!cancelled) {
                    setOrder(data ?? null);
                    setError("");
                }
            })
            .catch((e) => {
                if (!cancelled) {
                    setError("Failed to load order: " + e);
                    console.error(e);
                }
            })
            .finally(() => {
                if (!cancelled) {
                    setLoading(false);
                }
            });

        return () => {
            cancelled = true;
        };
    }, [refreshIndex]);

    const reload = () => {
        setLoading(true);
        setRefreshIndex((i) => i + 1);
    }

    const handleAccept = async () => {
        if (!order) return;

        try {
            await acceptOrder(order.id);
            reload();
        } catch (e) {
            setError("Failed to accept order: " + e);
            console.error(e);
        }
    };

    const handleReject = async () => {
        if (!order) return;

        try {
            await rejectOrder(order.id);
            reload();
        } catch (e) {
            setError("Failed to reject order: " + e);
            console.error(e);
        }
    };

    if (loading) {
        return <p>Loading...</p>
    }

    if (error) {
        return <p>{error}</p>
    }

    if (!order) {
        return (
            <div>
                <h1>Driver Dashboard</h1>
                <p>No order assigned</p>
            </div>
        )
    }

    return (
        <div>
            <h1>Driver Dashboard</h1>
            <DriverOrderCard
                order={order}
                onAccept={handleAccept}
                onReject={handleReject}/>
        </div>
    )

}