package com.karmazyn.logisticsdispatchsystem.order.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssignDriverRequestDto {
    @NotNull(message = "Driver ID is required")
    private Long driverId;
}
