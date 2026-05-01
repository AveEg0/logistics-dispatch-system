package com.karmazyn.logisticsdispatchsystem.common.audit.dto;

import com.karmazyn.logisticsdispatchsystem.common.audit.entity.SecurityAction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SecurityLogRequestDto {

    private SecurityAction action;
    private Long userId;
    private String email;

    private boolean success;

    private String ipAddress;
    private String userAgent;

    private String requestUri;
    private String httpMethod;

    private String details;
}
