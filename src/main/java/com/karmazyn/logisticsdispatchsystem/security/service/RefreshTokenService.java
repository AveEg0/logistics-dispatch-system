package com.karmazyn.logisticsdispatchsystem.security.service;

import com.karmazyn.logisticsdispatchsystem.common.exception.InvalidRefreshTokenException;
import com.karmazyn.logisticsdispatchsystem.common.exception.RefreshTokenExpiredException;
import com.karmazyn.logisticsdispatchsystem.common.exception.RefreshTokenRevokedException;
import com.karmazyn.logisticsdispatchsystem.security.config.JwtProperties;
import com.karmazyn.logisticsdispatchsystem.security.entity.RefreshToken;
import com.karmazyn.logisticsdispatchsystem.security.repository.RefreshTokenRepository;
import com.karmazyn.logisticsdispatchsystem.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProperties jwtProperties;

    // CREATE
    public RefreshToken create(User user) {
        RefreshToken token = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expiryDate(Instant.now().plus(jwtProperties.getRefreshExpirationTime()))
                .revoked(false)
                .build();

        return refreshTokenRepository.save(token);
    }

    public RefreshToken validate(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidRefreshTokenException("Invalid refresh token"));

        if (refreshToken.isRevoked()) {
            throw new RefreshTokenRevokedException("Refresh token revoked");
        }

        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            throw new RefreshTokenExpiredException("Refresh token expired");
        }

        return refreshToken;
    }

    @Transactional
    public RefreshToken rotate(RefreshToken oldToken) {

        oldToken.setRevoked(true);
        refreshTokenRepository.save(oldToken);

        return create(oldToken.getUser());
    }

    @Transactional
    public void revokeAllByUser(User user) {
        List<RefreshToken> tokens =
                refreshTokenRepository.findAllByUserId(user.getId());

        tokens.forEach(t -> t.setRevoked(true));

        refreshTokenRepository.saveAll(tokens);
    }
}