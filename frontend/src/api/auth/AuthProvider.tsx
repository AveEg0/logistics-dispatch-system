import React, { useEffect, useState} from "react";
import type {User} from "../userApi.ts";
import {fetchMe, setCurrentUser} from "./authService.ts";
import { AuthContext } from "./useAuth.ts";
import {clearAccessToken, setAccessToken} from "../../utils/token.ts";
import {refresh} from "../auth.ts";

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

            try {
                const authResponse = await refresh();
                setAccessToken(authResponse.accessToken)

                const me = await fetchMe();
                setUserState(me);
            } catch (e) {
                console.error("Failed to refresh token:", e);
                clearAccessToken();
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