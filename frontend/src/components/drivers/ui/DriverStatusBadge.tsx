import type {DriverStatus} from "../../../types/drivers/driverStatus.ts";

type Props = {
    status: DriverStatus;
};

const styles: Record<DriverStatus, {background: string, color: string}> = {
    AVAILABLE: {
        background: "#dcfce7",
        color: "#16a34a",
    },

    BUSY: {
        background: "#fef3c7",
        color: "#d97706",
    },

    OFFLINE: {
        background: "#fee2e2",
        color: "#dc2626",
    },

    RESERVED: {
        background: "#e0f2fe",
        color: "#2563eb",
    }
};


export const DriverStatusBadge = ({ status }: Props) => {
    const style = styles[status];

    return (
        <span style={{
        ...style,
        padding: "4px 8px",
        borderRadius: "6px",
        fontSize: "12px",
        fontWeight: 600,
        width: "fit-content",
        textAlign: "center",
            ...style
        }
        }
        >
            {status}
            </span>
    );
}