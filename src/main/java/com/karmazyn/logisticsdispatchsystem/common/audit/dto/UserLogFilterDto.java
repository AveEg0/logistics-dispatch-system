package com.karmazyn.logisticsdispatchsystem.common.audit.dto;

import com.karmazyn.logisticsdispatchsystem.common.audit.entity.UserAction;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class UserLogFilterDto {
    private Long userId;
    private String email;
    private UserAction action;
    private Boolean success;
    private String entity;
    private OffsetDateTime from;
    private OffsetDateTime to;
}
