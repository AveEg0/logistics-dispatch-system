import { api } from "./axios";
import type {LoginRequest, AuthResponse} from "../types/auth";

export const login = async (data: LoginRequest): Promise<AuthResponse> => {
    const response = await api.post<AuthResponse>("/auth/login", data);
    return response.data;
};