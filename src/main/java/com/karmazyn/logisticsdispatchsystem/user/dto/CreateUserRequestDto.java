package com.karmazyn.logisticsdispatchsystem.user.dto;

import com.karmazyn.logisticsdispatchsystem.user.entity.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Data Transfer Object for creating a new user.
 */
@Data
@Schema(description = "Request object for creating a new system user")
public class CreateUserRequestDto {
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Schema(description = "User's email address", example = "john.doe@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Schema(description = "User's password (min 8 characters)", example = "SecurePass123!", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    @NotNull(message = "Role is required")
    @Schema(description = "Assigned user role", example = "DISPATCHER", requiredMode = Schema.RequiredMode.REQUIRED)
    private UserRole role;
}
