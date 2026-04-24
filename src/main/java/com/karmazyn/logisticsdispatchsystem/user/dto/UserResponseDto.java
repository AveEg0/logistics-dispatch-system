package com.karmazyn.logisticsdispatchsystem.user.dto;

import com.karmazyn.logisticsdispatchsystem.user.entity.UserRole;
import lombok.Data;
import java.time.OffsetDateTime;

@Data
public class UserResponseDto {
    private Long id;
    private String email;
    private UserRole role;
    private boolean enabled;
    private OffsetDateTime createdAt;
}
