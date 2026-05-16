import type {Driver} from "../../api/driverApi.ts";
import {useState} from "react";
import {assignDriver} from "../../api/ordersApi.ts";

type Props = {
    orderId: number;
    drivers: Driver[];
    onAssigned: () => void;
};

export const AssignDriverAction = ({
                                       orderId, drivers, onAssigned }: Props) => {

    const [selectedDriver, setSelectedDriver] = useState('');
    const [loading, setLoading] = useState(false);

    const handleAssign = async () => {
        if (!selectedDriver) return;

        try {
            setLoading(true);

            await assignDriver(orderId, Number(selectedDriver));

            onAssigned();
        } catch (e) {
            console.error("Failed to assign driver:", e);
        } finally {
            setLoading(false);
        }
    };



    return (
        <div
        style={{display: "flex", gap: "12px", marginBottom: "20px"}}>

        <select
            value={selectedDriver}
            onChange={(e) => setSelectedDriver(e.target.value)}>
            <option value="">Assign Driver</option>
            {drivers.map((driver) => (
                <option key={driver.id}
                        value={driver.id}>
                    {driver.name}
                </option>
            ))}
        </select>

            <button onClick={handleAssign} disabled={!selectedDriver || loading}>
                { loading ? "Assigning..." : "Assign"}
            </button>

        </div>
    )
}