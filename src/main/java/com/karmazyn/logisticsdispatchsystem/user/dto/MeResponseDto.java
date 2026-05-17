package com.karmazyn.logisticsdispatchsystem.user.dto;

import com.karmazyn.logisticsdispatchsystem.user.entity.UserRole;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MeResponseDto {
    private Long id;
    private String email;
    private UserRole role;
    private Long driverId;
}
