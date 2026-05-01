package com.karmazyn.logisticsdispatchsystem.common.audit.service;

import com.karmazyn.logisticsdispatchsystem.common.audit.entity.UserAction;
import com.karmazyn.logisticsdispatchsystem.common.audit.entity.UserLog;
import com.karmazyn.logisticsdispatchsystem.common.audit.repository.UserLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final UserLogRepository userLogRepository;

    public void log(
            UserAction action,
            Long userId,
            String email,
            String entity,
            Long entityId,
            String details,
            String ip,
            String userAgent,
            String uri,
            String method
    ) {
        UserLog log = UserLog.builder()
                .userId(userId)
                .email(email)
                .action(action)
                .entity(entity)
                .entityId(entityId)
                .details(details)
                .ipAddress(ip)
                .userAgent(userAgent)
                .requestUri(uri)
                .httpMethod(method)
                .build();

        userLogRepository.save(log);
    }


}