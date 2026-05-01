package com.karmazyn.logisticsdispatchsystem.user.dto;

import com.karmazyn.logisticsdispatchsystem.user.entity.UserRole;
import lombok.Data;

@Data
public class UserFilterDto {
    String email;
    UserRole role;
}
