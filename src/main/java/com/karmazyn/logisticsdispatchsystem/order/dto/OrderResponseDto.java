package com.karmazyn.logisticsdispatchsystem.order.dto;

import com.karmazyn.logisticsdispatchsystem.common.audit.dto.Identifiable;
import com.karmazyn.logisticsdispatchsystem.order.entity.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import java.time.OffsetDateTime;

/**
 * Data Transfer Object for delivery order details.
 */
@Data
@Builder
@Schema(description = "Response object representing detailed order information")
public class OrderResponseDto implements Identifiable {
    @Schema(description = "Unique identifier of the order", example = "1001")
    private Long id;

    @Schema(description = "Pickup location address or coordinates", example = "123 Main St, New York, NY")
    private String pickupLocation;

    @Schema(description = "Delivery location address or coordinates", example = "456 Side St, New York, NY")
    private String deliveryLocation;

    @Schema(description = "Description or special instructions for the order", example = "Fragile items")
    private String description;

    @Schema(description = "Current status of the order", example = "PENDING")
    private OrderStatus status;

    @Schema(description = "ID of the assigned driver (if any)", example = "10")
    private Long driverId;

    @Schema(description = "Name of the assigned driver (if any)", example = "Michael Smith")
    private String driverName;

    @Schema(description = "ID of the user who placed the order", example = "3")
    private Long createdByUserId;

    @Schema(description = "Timestamp when the order was created", example = "2023-10-27T10:00:00Z")
    private OffsetDateTime createdAt;

    @Schema(description = "Operational comment (cancellation reason or completion notes)", example = "Package delivered to front desk")
    private String comment;
}
