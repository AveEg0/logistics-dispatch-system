package com.karmazyn.logisticsdispatchsystem.order.dto;

import com.karmazyn.logisticsdispatchsystem.order.entity.OrderStatus;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class OrderFilterDto {

    private String search;

    private OrderStatus status;

    private Long driverId;

    private OffsetDateTime from;

    private OffsetDateTime to;
}
