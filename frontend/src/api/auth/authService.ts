import type {User} from "../userApi.ts";
import {api} from "../apiClient.ts";

let currentUser: User | null = null;

export const setCurrentUser = (user: User | null) => {
    currentUser = user;
}

export const getCurrentUser = () => currentUser;

export const getUserRoles = () => currentUser?.role ?? null;

export const fetchMe = async (): Promise<User | null> =>
    await api.get("/users/me").then(res => res.data);