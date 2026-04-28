package com.karmazyn.logisticsdispatchsystem.driver.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Data Transfer Object for creating a new driver profile.
 */
@Data
@Schema(description = "Request object for creating a new driver profile linked to a user account")
public class CreateDriverRequestDto {
    @NotBlank(message = "Name is required")
    @Schema(description = "Full name of the driver", example = "Michael Smith", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotNull(message = "User ID is required")
    @Schema(description = "ID of the associated user account (must have DRIVER role)", example = "5", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long userId;
}
