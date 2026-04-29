package com.karmazyn.logisticsdispatchsystem.security.controller;

import com.karmazyn.logisticsdispatchsystem.security.dto.RefreshRequest;
import com.karmazyn.logisticsdispatchsystem.security.service.AuthService;
import com.karmazyn.logisticsdispatchsystem.security.dto.LoginRequest;
import com.karmazyn.logisticsdispatchsystem.security.dto.AuthResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @Operation(summary = "Login user", description = "Authenticates a user and returns a JWT access token and a refresh token")
    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @Operation(summary = "Refresh access token", description = "Uses a refresh token to obtain a new JWT access token and a new refresh token (token rotation)")
    @PostMapping("/refresh")
    public AuthResponse refresh(@RequestBody RefreshRequest request) {
        return authService.refresh(request.getRefreshToken());
    }

    @Operation(summary = "Logout user", description = "Invalidates the user's current session and revokes refresh tokens")
    @PostMapping("/logout")
    public void logout() {
        authService.logout();
    }

}