package com.karmazyn.logisticsdispatchsystem.driver.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateDriverCurrentLocationDto {
    @NotBlank(message = "Current location is required")
    private String currentLocation;
}
