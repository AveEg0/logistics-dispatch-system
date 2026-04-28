package com.karmazyn.logisticsdispatchsystem.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Data Transfer Object for completing an order.
 */
@Data
@Schema(description = "Request object for completing a delivery order")
public class CompleteOrderRequestDto {
    @Size(max = 500, message = "Comment must not exceed 500 characters")
    @Schema(description = "Optional completion notes or delivery feedback", example = "Package delivered to front desk")
    private String comment;
}
