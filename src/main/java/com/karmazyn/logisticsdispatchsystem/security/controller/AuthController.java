package com.karmazyn.logisticsdispatchsystem.security.controller;

import com.karmazyn.logisticsdispatchsystem.security.service.AuthService;
import com.karmazyn.logisticsdispatchsystem.security.dto.LoginRequest;
import com.karmazyn.logisticsdispatchsystem.security.dto.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}