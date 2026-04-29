package com.karmazyn.logisticsdispatchsystem.security.service;

import com.karmazyn.logisticsdispatchsystem.security.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenCleanupService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Scheduled(cron = "0 0 3 * * *")
    public void cleanupTokens() {
        refreshTokenRepository.deleteExpired();
    }
}
