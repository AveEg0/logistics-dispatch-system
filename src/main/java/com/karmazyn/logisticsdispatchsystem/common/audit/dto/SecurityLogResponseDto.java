package com.karmazyn.logisticsdispatchsystem.common.audit.dto;

import lombok.Data;
import java.time.OffsetDateTime;

@Data
public class SecurityLogResponseDto {

    private Long id;
    private Long userId;
    private String email;
    private String action;
    private boolean success;

    private String ipAddress;
    private String userAgent;
    private String requestUri;
    private String httpMethod;

    private String details;
    private OffsetDateTime createdAt;
}
