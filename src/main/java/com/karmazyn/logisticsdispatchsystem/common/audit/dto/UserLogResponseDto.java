package com.karmazyn.logisticsdispatchsystem.common.audit.dto;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class UserLogResponseDto {

    private Long id;
    private Long userId;
    private String email;
    private String action;

    private String entity;
    private Long entityId;

    private String ipAddress;
    private String requestUri;
    private String httpMethod;

    private String details;
    private OffsetDateTime createdAt;
}
