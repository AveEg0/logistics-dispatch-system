import React from "react";
import ReactDOM from "react-dom/client";
import "./index.css";
import {RouterProvider} from "react-router-dom";
import {router} from "./router";
import {AuthProvider} from "./api/auth/AuthProvider.tsx";

const rootElement = document.getElementById("root");

if (!rootElement) {
    document.body.innerHTML = "<h1 style='color: red'>Root element not found</h1>";
    throw new Error("Root element with id='root' was not found");
}

ReactDOM.createRoot(rootElement).render(
    <React.StrictMode>
        <AuthProvider>
            <RouterProvider router={router}/>
        </AuthProvider>
    </React.StrictMode>
);