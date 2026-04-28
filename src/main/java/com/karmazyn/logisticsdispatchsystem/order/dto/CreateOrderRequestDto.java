package com.karmazyn.logisticsdispatchsystem.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Data Transfer Object for creating a new delivery order.
 */
@Data
@Schema(description = "Request object for creating a new delivery order")
public class CreateOrderRequestDto {
    @NotBlank(message = "Pickup location is required")
    @Schema(description = "Address or coordinates where the package should be picked up", example = "123 Main St, New York, NY", requiredMode = Schema.RequiredMode.REQUIRED)
    private String pickupLocation;

    @NotBlank(message = "Delivery location is required")
    @Schema(description = "Address or coordinates where the package should be delivered", example = "456 Side St, New York, NY", requiredMode = Schema.RequiredMode.REQUIRED)
    private String deliveryLocation;

    @Schema(description = "Optional description of the package or special instructions", example = "Fragile items, handle with care")
    private String description;

    @NotNull(message = "User ID is required")
    @Schema(description = "ID of the user who is placing the order", example = "3", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long createdByUserId;
}
