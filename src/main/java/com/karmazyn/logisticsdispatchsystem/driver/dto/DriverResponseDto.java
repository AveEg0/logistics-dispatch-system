package com.karmazyn.logisticsdispatchsystem.driver.dto;

import com.karmazyn.logisticsdispatchsystem.common.audit.dto.Identifiable;
import com.karmazyn.logisticsdispatchsystem.driver.entity.DriverStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * Data Transfer Object for driver profile details.
 */
@Data
@Builder
@Schema(description = "Response object representing driver details")
public class DriverResponseDto implements Identifiable {
    @Schema(description = "Unique identifier of the driver profile", example = "1")
    private Long id;

    @Schema(description = "Full name of the driver", example = "Michael Smith")
    private String name;

    @Schema(description = "Current availability status of the driver", example = "AVAILABLE")
    private DriverStatus status;

    @Schema(description = "Last known location of the driver", example = "40.7128° N, 74.0060° W")
    private String currentLocation;

    @Schema(description = "Email address from the associated user account", example = "michael.smith@example.com")
    private String email;
}
