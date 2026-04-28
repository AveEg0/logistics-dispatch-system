package com.karmazyn.logisticsdispatchsystem.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Data Transfer Object for updating user password.
 */
@Data
@Schema(description = "Request object for updating user password")
public class UpdatePasswordRequestDto {

    @NotBlank(message = "Old password is required")
    @Schema(description = "User's current password", example = "OldSecurePass123!", requiredMode = Schema.RequiredMode.REQUIRED)
    private String oldPassword;

    @NotBlank(message = "New password is required")
    @Size(min = 8, message = "New password must be at least 8 characters long")
    @Schema(description = "User's new password (min 8 characters)", example = "NewSecurePass456!", requiredMode = Schema.RequiredMode.REQUIRED)
    private String newPassword;
}
