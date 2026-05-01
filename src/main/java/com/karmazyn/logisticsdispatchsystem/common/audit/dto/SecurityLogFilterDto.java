package com.karmazyn.logisticsdispatchsystem.common.audit.dto;

import com.karmazyn.logisticsdispatchsystem.common.audit.entity.SecurityAction;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class SecurityLogFilterDto {

    private Long userId;
    private String email;
    private SecurityAction action;
    private Boolean success;

    private OffsetDateTime from;
    private OffsetDateTime to;
}
