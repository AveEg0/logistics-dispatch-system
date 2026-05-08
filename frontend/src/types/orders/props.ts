import type {Order} from "../../api/ordersApi.ts";

export type OrderProps = {
    orders: Order[];
    onSort: (field: string) => void;
    sort: { field: string; direction: "asc" | "desc" };
};