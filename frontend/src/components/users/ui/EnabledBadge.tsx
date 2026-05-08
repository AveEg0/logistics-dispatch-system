export const EnabledBadge = ({ enabled }: { enabled: boolean }) => {
    return (
        <span
            style={{
                padding: "4px 8px",
                borderRadius: 6,
                background: enabled ? "green" : "red",
                color: "white",
            }}
        >
      {enabled ? "ACTIVE" : "DISABLED"}
    </span>
    );
};