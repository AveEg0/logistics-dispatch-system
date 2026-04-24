package com.karmazyn.logisticsdispatchsystem.user.dto;

import com.karmazyn.logisticsdispatchsystem.user.entity.UserRole;
import lombok.Data;

@Data
public class CreateUserRequestDto {
    private String email;
    private String password;
    private UserRole role;
}
