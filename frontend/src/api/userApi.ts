import type {UserRole} from "../types/user/userRole.ts";
import {api} from "./apiClient.ts";


export interface User {
    id: number;
    email: string;
    role: UserRole;
    enabled: boolean;
    createdAt: string;
}

export interface UserFilter {
    email?: string;
    role?: string;
}

export const fetchUsers = async (
    page: number,
    size: number,
    sort: { field: string; direction: string },
    filters: UserFilter
) => {
    const res = await api.get("/users", {
        params: {
            page,
            size,
            sort: `${sort.field},${sort.direction}`,
            ...(filters.email && { email: filters.email }),
            ...(filters.role && { role: filters.role }),
        },
        headers: {
            Authorization: `Bearer ${localStorage.getItem("accessToken")}`,
        }
    });

    return res.data;
};