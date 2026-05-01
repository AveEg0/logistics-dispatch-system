package com.karmazyn.logisticsdispatchsystem.user.repository;

import com.karmazyn.logisticsdispatchsystem.user.entity.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT o FROM User o WHERE o.id = :id")
    Optional<User> findByIdForUpdate(Long id);
    Optional<User> findByEmail(String email);
}
