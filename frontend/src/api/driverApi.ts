import {api} from "./apiClient.ts";
import type {DriverStatus} from "../types/drivers/driverStatus.ts";

export interface Driver {
    id: number;
    name: string;
    status: DriverStatus
    currentLocation: string;
    email: string;
}

export interface DriverFilter {
    search?: string;
    status?: string;
}

export interface PageResponse<T> { data: T[]; totalElements: number; totalPages: number; }

export const fetchDrivers = async (
    page: number,
    size: number,
    sort: { field: string; direction: string },
    filters: DriverFilter) => {

    const res = await api.get("/drivers", {
        params: {
            page,
            size,
            sort: `${sort.field},${sort.direction}`,
            ...(filters.search && { search: filters.search }),
            ...(filters.status && { status: filters.status }),
        }
    });
    return res.data;
}