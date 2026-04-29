package com.karmazyn.logisticsdispatchsystem.security.repository;

import com.karmazyn.logisticsdispatchsystem.security.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    List<RefreshToken> findAllByUserId(Long userId);

    void deleteByUserId(Long userId);

    void deleteByToken(String token);

    // Delete expired refresh tokens
    @Modifying
    @Query("DELETE FROM RefreshToken t WHERE t.expiryDate < CURRENT_TIMESTAMP")
    void deleteExpired();
}
