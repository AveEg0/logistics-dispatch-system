package com.karmazyn.logisticsdispatchsystem.order.dto;

import com.karmazyn.logisticsdispatchsystem.order.entity.OrderStatus;
import lombok.Builder;
import lombok.Data;
import java.time.OffsetDateTime;

@Data
@Builder
public class OrderResponseDto {
    private Long id;

    private String pickupLocation;
    private String deliveryLocation;
    private String description;

    private OrderStatus status;

    private Long driverId;
    private String driverName;

    private Long userId;

    private OffsetDateTime createdAt;
}
