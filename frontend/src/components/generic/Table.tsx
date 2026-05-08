import React from "react";

type Column<T> = {
    key: keyof T;
    label: string;
    render?: (row: T) => React.ReactNode;
    sortable?: boolean;
};

type Props<T> = {
    data: T[];
    columns: Column<T>[];

    sort?: {
        field: string;
        direction: "asc" | "desc";
    };

    onSort?: (field: string) => void;
};

export function Table<T extends { id: number }>({
                                                    data,
                                                    columns,
                                                    sort,
                                                    onSort,
                                                }: Props<T>) {
    return (
        <table style={{ width: "100%", borderCollapse: "collapse" }}>
            <thead>
            <tr>
                {columns.map((col) => (
                    <th
                        key={String(col.key)}
                        onClick={() => col.sortable && onSort?.(String(col.key))}
                        style={{
                            padding: "12px",
                            borderBottom: "2px solid #e5e7eb",
                            cursor: col.sortable ? "pointer" : "default",
                        }}
                    >
                        {col.label}

                        {sort?.field === col.key && (
                            <span style={{ marginLeft: 6 }}>
                  {sort.direction === "asc" ? "▲" : "▼"}
                </span>
                        )}
                    </th>
                ))}
            </tr>
            </thead>

            <tbody>
            {data.map((row) => (
                <tr key={row.id}>
                    {columns.map((col) => (
                        <td
                            key={String(col.key)}
                            style={{
                                padding: "12px",
                                borderBottom: "1px solid #eee",
                            }}
                        >
                            {col.render
                                ? col.render(row)
                                : String(row[col.key])}
                        </td>
                    ))}
                </tr>
            ))}
            </tbody>
        </table>
    );
}