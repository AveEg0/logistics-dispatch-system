package com.karmazyn.logisticsdispatchsystem.order.mapper;

import com.karmazyn.logisticsdispatchsystem.order.dto.OrderResponseDto;
import com.karmazyn.logisticsdispatchsystem.order.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(source = "driver.id", target = "driverId")
    @Mapping(source = "driver.name", target = "driverName")
    @Mapping(source = "createdBy.id", target = "userId")
    OrderResponseDto toDto(Order order);
}
