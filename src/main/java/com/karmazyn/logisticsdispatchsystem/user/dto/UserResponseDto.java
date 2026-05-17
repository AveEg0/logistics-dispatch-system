package com.karmazyn.logisticsdispatchsystem.user.dto;

import com.karmazyn.logisticsdispatchsystem.common.audit.dto.Identifiable;
import com.karmazyn.logisticsdispatchsystem.user.entity.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.OffsetDateTime;

/**
 * Data Transfer Object for user response details.
 */
@Data
@Schema(description = "Response object representing user details")
public class UserResponseDto implements Identifiable {
    @Schema(description = "Unique identifier of the user", example = "1")
    private Long id;

    @Schema(description = "User's email address", example = "john.doe@example.com")
    private String email;

    @Schema(description = "Assigned user role", example = "DISPATCHER")
    private UserRole role;

    @Schema(description = "Indicates whether the user account is active", example = "true")
    private boolean enabled;

    @Schema(description = "Timestamp when the user account was created", example = "2023-10-27T10:00:00Z")
    private OffsetDateTime createdAt;
}
