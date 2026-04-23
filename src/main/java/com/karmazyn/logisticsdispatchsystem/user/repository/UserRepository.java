package com.karmazyn.logisticsdispatchsystem.user.repository;

import com.karmazyn.logisticsdispatchsystem.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
