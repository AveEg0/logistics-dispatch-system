import React, { useState } from "react";
import { login } from "../api/auth";
import {setTokens} from "../utils/token.ts";
import { useNavigate } from "react-router-dom";
import {fetchMe} from "../api/auth/authService.ts";
import {useAuth} from "../api/auth/useAuth.ts";

export function LoginPage() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const navigate = useNavigate();
    const {setUserState} = useAuth();

    const handleSubmit = async (e: React.SyntheticEvent) => {
        e.preventDefault();

        try {
            const response = await login({ email, password });
            console.log("RESPONSE:", response);
            console.log({
                accessToken: localStorage.getItem("accessToken"),
                refreshToken: localStorage.getItem("refreshToken"),
            });
            setTokens(response.accessToken, response.refreshToken)
            console.log({
                accessToken: localStorage.getItem("accessToken"),
                refreshToken: localStorage.getItem("refreshToken"),
            });

            const me = await fetchMe();
            setUserState(me);
            console.log("CURRENT USER:", me);

            navigate("/");

            console.log("SUCCESS:", response);

        } catch (error) {
            console.error("LOGIN ERROR", error);
        }
    };

    return (
        <div style={{ display: "flex", justifyContent: "center", marginTop: "100px" }}>
            <form onSubmit={handleSubmit}>
                <h2>Login</h2>

                <div>
                    <input
                        type="email"
                        placeholder="Email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                    />
                </div>

                <div>
                    <input
                        type="password"
                        placeholder="Password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                </div>

                <button type="submit">Login</button>
            </form>
        </div>
    );
}