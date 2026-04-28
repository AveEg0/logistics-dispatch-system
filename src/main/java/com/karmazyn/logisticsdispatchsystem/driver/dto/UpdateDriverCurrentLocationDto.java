package com.karmazyn.logisticsdispatchsystem.driver.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Data Transfer Object for updating driver's current location.
 */
@Data
@Schema(description = "Request object for updating driver's current location")
public class UpdateDriverCurrentLocationDto {
    @NotBlank(message = "Current location is required")
    @Schema(description = "New location coordinates or address", example = "34.0522° N, 118.2437° W", requiredMode = Schema.RequiredMode.REQUIRED)
    private String currentLocation;
}
