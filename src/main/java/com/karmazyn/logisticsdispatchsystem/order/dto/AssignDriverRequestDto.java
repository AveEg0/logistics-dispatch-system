package com.karmazyn.logisticsdispatchsystem.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Data Transfer Object for assigning a driver to an order.
 */
@Data
@Schema(description = "Request object for assigning a specific driver to an order")
public class AssignDriverRequestDto {
    @NotNull(message = "Driver ID is required")
    @Schema(description = "Unique identifier of the driver to be assigned", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long driverId;
}
