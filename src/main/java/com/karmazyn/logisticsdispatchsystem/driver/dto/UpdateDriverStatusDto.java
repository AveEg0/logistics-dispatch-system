package com.karmazyn.logisticsdispatchsystem.driver.dto;

import com.karmazyn.logisticsdispatchsystem.driver.entity.DriverStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Data Transfer Object for updating driver's availability status.
 */
@Data
@Schema(description = "Request object for updating driver's availability status")
public class UpdateDriverStatusDto {
    @NotNull(message = "Driver status is required")
    @Schema(description = "New availability status", example = "BUSY", requiredMode = Schema.RequiredMode.REQUIRED)
    private DriverStatus driverStatus;
}
