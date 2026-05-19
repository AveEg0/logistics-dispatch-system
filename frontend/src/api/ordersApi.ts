import {api} from "./apiClient.ts";
import type {OrderStatus} from "../types/orders/orderStatus.ts";

export interface Order {
    id: number;
    pickupLocation: string;
    deliveryLocation: string;
    description: string;
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

export interface CreateOrderRequest {
    pickupLocation: string;
    deliveryLocation: string;
    description: string;
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
        }
    });

    return res.data;
};

export const createOrder = async (body: CreateOrderRequest) => {
    const res = await api.post("/orders", body);

    return res.data;
}

export const assignDriver = async (
    orderId: number, driverId: number) => {
    const res = await api.put(
        `/orders/${orderId}/assign`,
        {driverId});
    return res.data
}

export const fetchMyCurrentOrder = async () => {
    const res = await api.get("/orders/my-current");
    return res.data;
}

export const acceptOrder = async (orderId: number) => {
    const res = await api.put(`/orders/${orderId}/accept`);
    return res.data;
}

export const rejectOrder = async (orderId: number) => {
    const res = await api.put(`/orders/${orderId}/reject`);
    return res.data;
}

export const completeOrder = async (orderId: number, data?: {comment?: string}) => {
    const res = await api.put(`/orders/${orderId}/complete`, data ?? {});
    return res.data;
}
