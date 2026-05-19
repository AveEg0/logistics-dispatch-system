package com.karmazyn.logisticsdispatchsystem.security.controller;

import com.karmazyn.logisticsdispatchsystem.security.service.AuthService;
import com.karmazyn.logisticsdispatchsystem.security.dto.LoginRequest;
import com.karmazyn.logisticsdispatchsystem.security.dto.AuthResponse;
import com.karmazyn.logisticsdispatchsystem.security.utils.CookieUtils;
import com.karmazyn.logisticsdispatchsystem.security.utils.LoginResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user authentication and token management")
public class AuthController {

    private final AuthService authService;
    private final CookieUtils cookieUtils;

    @Operation(summary = "Login user", description = "Authenticates a user and returns a JWT access token and a refresh token")
    @PostMapping("/login")
    public AuthResponse login(
            @RequestBody LoginRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse response) {

        LoginResult loginResult = authService.login(request, httpRequest);
        cookieUtils.setRefreshTokenCookie(response, loginResult.getRefreshToken());
        return new AuthResponse(loginResult.getAccessToken());
    }

    @Operation(summary = "Refresh access token", description = "Uses a refresh token to obtain a new JWT access token and a new refresh token (token rotation)")
    @PostMapping("/refresh")
    public AuthResponse refresh(
            HttpServletRequest httpRequest,
            HttpServletResponse response) {

        String refreshToken = cookieUtils.extractRefreshTokenFromCookie(httpRequest);
        LoginResult loginResult = authService.refresh(refreshToken, httpRequest);
        return new AuthResponse(loginResult.getAccessToken());
    }

    @Operation(summary = "Logout user", description = "Invalidates the user's current session and revokes refresh tokens")
    @PostMapping("/logout")
    public void logout(HttpServletRequest httpRequest, HttpServletResponse response) {
        authService.logout(httpRequest);
        cookieUtils.clearRefreshTokenCookie(response);
    }

}