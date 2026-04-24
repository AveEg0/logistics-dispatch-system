package com.karmazyn.logisticsdispatchsystem.driver.dto;

import com.karmazyn.logisticsdispatchsystem.driver.entity.DriverStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateDriverStatusDto {
    @NotNull(message = "Driver status is required")
    private DriverStatus driverStatus;
}
