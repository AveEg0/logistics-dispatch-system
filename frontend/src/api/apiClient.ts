import axios from "axios";
import {clearTokens, getAccessToken, getRefreshToken, setTokens} from "../utils/token.ts";
import {refresh} from "./auth";

export const api = axios.create({
    baseURL: "http://localhost:8080",
});

let isRefreshing = false;
let failedQueue: any[] = [];

const processQueue = (token: string | null) => {
    failedQueue.forEach((prom) => {
        if (token) {
            prom.resolve(token);
        } else {
          prom.reject();
        }
    });
    failedQueue = [];
};

api.interceptors.request.use((config) => {
    const token = getAccessToken();
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

api.interceptors.response.use(
    response => response,
    async error => {
        console.log("INTERCEPTOR HIT", error.response?.status);
        const originalRequest = error.config;
        if (error.response?.status === 401 && !originalRequest._retry) {
            originalRequest._retry = true;

            if (isRefreshing) {
                return new Promise((resolve, reject) => {
                    failedQueue.push({ resolve, reject });
                }).then(token => {
                    originalRequest.headers.Authorization = `Bearer ${token}`;
                    return api(originalRequest);
                });
            }

            isRefreshing = true;

            try {
                const refreshToken = getRefreshToken();

                if (!refreshToken) {
                    throw new Error('Refresh token not found');
                }
                const authResponse = await refresh(refreshToken);
                const newAccessToken = authResponse.accessToken;
                const newRefreshToken = authResponse.refreshToken;

                setTokens(newAccessToken, newRefreshToken);

                api.defaults.headers.common['Authorization'] = `Bearer ${newAccessToken}`;

                processQueue(newAccessToken);

                originalRequest.headers.Authorization = `Bearer ${newAccessToken}`;
                return api(originalRequest);
            } catch (e) {
                processQueue(null);
                console.error('Refresh token error:', e);
                clearTokens();
                window.location.href = "/login";
                return Promise.reject(e);
            } finally {
                isRefreshing = false;
            }
        }
        return Promise.reject(error);
    });