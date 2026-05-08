import {api} from "./apiClient.ts";
import type {OrderStatus} from "../types/orders/orderStatus.ts";

export interface Order {
    id: number;
    pickupLocation: string;
    deliveryLocation: string;
    status: OrderStatus;
    driverName?: string;
    createdAt: string;
}

export interface OrderFilter {
    search?: string;
    status?: string;
    driverId?: string;
    from?: string;
    to?: string;
}

export const fetchOrders = async (
    page: number,
    size: number,
    sort: { field: string; direction: string },
    filters: OrderFilter
) => {
    const res = await api.get("/orders", {
        params: {
            page,
            size,
            sort: `${sort.field},${sort.direction}`,
            ...(filters.search && { search: filters.search }),
            ...(filters.status && { status: filters.status }),
            ...(filters.driverId && { driverId: filters.driverId }),
            ...(filters.from && { from: filters.from }),
            ...(filters.to && { to: filters.to }),
        },
        headers: {
            Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
        },
    });

    return res.data;
};