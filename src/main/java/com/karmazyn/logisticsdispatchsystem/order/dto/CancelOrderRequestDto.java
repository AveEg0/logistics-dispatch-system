package com.karmazyn.logisticsdispatchsystem.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Data Transfer Object for canceling an order.
 */
@Data
@Schema(description = "Request object for canceling an order")
public class CancelOrderRequestDto {
    @Schema(description = "Reason for canceling the order", example = "Incorrect address provided")
    private String comment;
}
