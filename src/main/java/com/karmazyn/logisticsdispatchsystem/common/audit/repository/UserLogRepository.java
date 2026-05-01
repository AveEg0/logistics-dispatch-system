package com.karmazyn.logisticsdispatchsystem.common.audit.repository;

import com.karmazyn.logisticsdispatchsystem.common.audit.entity.UserLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserLogRepository extends JpaRepository<UserLog, Long>, JpaSpecificationExecutor<UserLog> {
}
