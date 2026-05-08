import { api } from "./apiClient";
import type {LoginRequest, AuthResponse} from "../types/auth";

export const login = async (data: LoginRequest) : Promise<AuthResponse> => {
    const response = await api.post<AuthResponse>("/auth/login", data);
    return response.data;
};

export const refresh = async (
    refreshToken: string) : Promise<AuthResponse> => {
    const response = await api.post<AuthResponse>("/auth/refresh", {refreshToken});
    return response.data;

};