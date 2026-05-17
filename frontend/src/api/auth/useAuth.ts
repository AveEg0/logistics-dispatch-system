import {createContext, useContext} from "react";
import type {User} from "../userApi.ts";

type AuthContextType = {
    user: User | null;
    isLoading: boolean;
    setUserState: (user: User | null) => void
};

export const AuthContext = createContext<AuthContextType | null>(null);

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error("useAuth must be used within an AuthProvider");
    }
    return context;
}