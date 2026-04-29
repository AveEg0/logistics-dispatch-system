package com.karmazyn.logisticsdispatchsystem.security.service;

import com.karmazyn.logisticsdispatchsystem.common.exception.InvalidPasswordException;
import com.karmazyn.logisticsdispatchsystem.common.exception.UserNotFoundException;
import com.karmazyn.logisticsdispatchsystem.security.dto.LoginRequest;
import com.karmazyn.logisticsdispatchsystem.security.dto.AuthResponse;
import com.karmazyn.logisticsdispatchsystem.security.entity.RefreshToken;
import com.karmazyn.logisticsdispatchsystem.security.utils.SecurityUtils;
import com.karmazyn.logisticsdispatchsystem.user.entity.User;
import com.karmazyn.logisticsdispatchsystem.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final SecurityUtils securityUtils;

    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new InvalidPasswordException("Invalid password");
        }

        String accessToken = jwtService.generateToken(user);
        RefreshToken refreshToken = refreshTokenService.create(user);

        return new AuthResponse(accessToken, refreshToken.getToken());
    }

    public AuthResponse refresh(String refreshToken) {

        RefreshToken token = refreshTokenService.validate(refreshToken);

        User user = token.getUser();

        RefreshToken newRefresh = refreshTokenService.rotate(token);
        String newAccess = jwtService.generateToken(user);

        return new AuthResponse(newAccess, newRefresh.getToken());
    }

    public void logout() {
        User user = securityUtils.getCurrentUser();
        refreshTokenService.revokeAllByUser(user);
    }
}
