package com.karmazyn.logisticsdispatchsystem.security.service;

import com.karmazyn.logisticsdispatchsystem.common.audit.dto.SecurityLogRequestDto;
import com.karmazyn.logisticsdispatchsystem.common.audit.entity.SecurityAction;
import com.karmazyn.logisticsdispatchsystem.common.audit.service.SecurityLogService;
import com.karmazyn.logisticsdispatchsystem.common.exception.InvalidPasswordException;
import com.karmazyn.logisticsdispatchsystem.common.exception.InvalidPrincipalException;
import com.karmazyn.logisticsdispatchsystem.common.exception.UserNotFoundException;
import com.karmazyn.logisticsdispatchsystem.security.dto.LoginRequest;
import com.karmazyn.logisticsdispatchsystem.security.dto.AuthResponse;
import com.karmazyn.logisticsdispatchsystem.security.entity.RefreshToken;
import com.karmazyn.logisticsdispatchsystem.security.utils.LoginResult;
import com.karmazyn.logisticsdispatchsystem.security.utils.SecurityUtils;
import com.karmazyn.logisticsdispatchsystem.user.entity.User;
import com.karmazyn.logisticsdispatchsystem.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final SecurityLogService securityLogService;
    private final String INVALID_PASSWORD = "INVALID_PASSWORD";
    private final String USER_NOT_FOUND = "USER_NOT_FOUND";
    private final String SUCCESS = "SUCCESS";
    private final String INVALID_PRINCIPAL = "INVALID_PRINCIPAL";

    public LoginResult login(LoginRequest request, HttpServletRequest httpRequest) {

        String ip = httpRequest.getRemoteAddr();
        String userAgent = httpRequest.getHeader("User-Agent");

        try {
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> {
                        securityLogService.log(
                                securityLogService.buildLog(
                                        SecurityAction.LOGIN,
                                        null,
                                        null,
                                        false,
                                        httpRequest,
                                        USER_NOT_FOUND
                                )
                        );
                        return new UserNotFoundException(USER_NOT_FOUND);
                    });

            if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
                securityLogService.log(
                        securityLogService.buildLog(
                                SecurityAction.LOGIN,
                                user.getId(),
                                user.getEmail(),
                                false,
                                httpRequest,
                                INVALID_PASSWORD
                        )
                );
                throw new InvalidPasswordException(INVALID_PASSWORD);
            }

            String accessToken = jwtService.generateToken(user);
            RefreshToken refreshToken = refreshTokenService.create(user);

            securityLogService.log(
                    securityLogService.buildLog(
                            SecurityAction.LOGIN,
                            user.getId(),
                            user.getEmail(),
                            true,
                            httpRequest,
                            SUCCESS
                    )
            );

            return new LoginResult(accessToken, refreshToken.getToken());

        } catch (Exception e) {

            securityLogService.log(
            securityLogService.buildLog(
                            SecurityAction.LOGIN,
                            null,
                            request.getEmail(),
                            false,
                            httpRequest,
                            e.getMessage()
                    )
            );
            throw e;
        }
    }

    public LoginResult refresh(String refreshToken, HttpServletRequest request) {

        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        try {
            RefreshToken token = refreshTokenService.validate(refreshToken);
            User user = token.getUser();

            RefreshToken newRefresh = refreshTokenService.rotate(token);
            String newAccess = jwtService.generateToken(user);

            securityLogService.log(
                    new SecurityLogRequestDto(
                            SecurityAction.REFRESH_TOKEN,
                            user.getId(),
                            user.getEmail(),
                            true,
                            ip,
                            userAgent,
                            "/auth/refresh",
                            "POST",
                            SUCCESS
                    )
            );

            return new LoginResult(newAccess, newRefresh.getToken());

        } catch (Exception e) {

            securityLogService.log(
                    new SecurityLogRequestDto(
                            SecurityAction.REFRESH_TOKEN,
                            null,
                            null,
                            false,
                            ip,
                            userAgent,
                            "/auth/refresh",
                            "POST",
                            e.getMessage()
                    )
            );

            throw e;
        }
    }

    public void logout(HttpServletRequest request) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            securityLogService.log(
                    securityLogService.buildLog(
                            SecurityAction.LOGOUT,
                            null,
                            null,
                            false,
                            request,
                            INVALID_PRINCIPAL
                    )
            );
            throw new InvalidPrincipalException(INVALID_PRINCIPAL);
        }

        String email = auth.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    securityLogService.log(
                            securityLogService.buildLog(
                                    SecurityAction.LOGOUT,
                                    null,
                                    null,
                                    false,
                                    request,
                                    USER_NOT_FOUND
                            )
                    );
                    return new UserNotFoundException(USER_NOT_FOUND);
                });

        refreshTokenService.revokeAllByUser(user);

        String ip = request != null ? request.getRemoteAddr() : null;
        String userAgent = request != null ? request.getHeader("User-Agent") : null;

        securityLogService.log(
                new SecurityLogRequestDto(
                        SecurityAction.LOGOUT,
                        user.getId(),
                        user.getEmail(),
                        true,
                        ip,
                        userAgent,
                        "/auth/logout",
                        "POST",
                        SUCCESS
                )
        );
    }

    private SecurityLogRequestDto buildLog(
            SecurityAction action,
            Long userId,
            String email,
            boolean success,
            HttpServletRequest request,
            String details
    ) {
        return new SecurityLogRequestDto(
                action,
                userId,
                email,
                success,
                request != null ? request.getRemoteAddr() : null,
                request != null ? request.getHeader("User-Agent") : null,
                request != null ? request.getRequestURI() : null,
                request != null ? request.getMethod() : null,
                details
        );
    }
}
