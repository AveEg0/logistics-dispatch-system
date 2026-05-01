package com.karmazyn.logisticsdispatchsystem.common.audit.repository;

import com.karmazyn.logisticsdispatchsystem.common.audit.entity.SecurityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SecurityLogRepository
        extends JpaRepository<SecurityLog, Long>,
        JpaSpecificationExecutor<SecurityLog> {
}
