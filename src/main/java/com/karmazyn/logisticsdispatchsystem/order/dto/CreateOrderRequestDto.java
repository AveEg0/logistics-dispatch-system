package com.karmazyn.logisticsdispatchsystem.order.dto;

import lombok.Data;

@Data
public class CreateOrderRequestDto {
    private String pickupLocation;
    private String deliveryLocation;
    private String description;

    private Long userId;
}
