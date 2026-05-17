import React, { useEffect, useState} from "react";
import type {User} from "../userApi.ts";
import {fetchMe, setCurrentUser} from "./authService.ts";
import { AuthContext } from "./useAuth.ts";
import {getAccessToken} from "../../utils/token.ts";

type Props = { children: React.ReactNode };




export const AuthProvider = ({children}: Props) => {
    const [user, setUser] = useState<User | null>(null);
    const [isLoading, setIsLoading] = useState(true);

    const setUserState = (nextUser: User | null) => {
        setUser(nextUser);
        setCurrentUser(nextUser);
    };

    useEffect(() => {
        const init = async () => {
            const accessToken = getAccessToken();

            if (!accessToken) {
                setUserState(null);
                setIsLoading(false);
                return;
            }

            try {
                const me = await fetchMe();
                setUserState(me);
            } catch (e) {
                console.error("Failed to fetch user:", e);
                setUserState(null);
            } finally {
                setIsLoading(false);
            }
        };
        void init();
    }, []);

    return <AuthContext.Provider value={{ user, isLoading, setUserState}}>
        {children}
    </AuthContext.Provider>;
}