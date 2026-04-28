package com.karmazyn.logisticsdispatchsystem.order.mapper;

import com.karmazyn.logisticsdispatchsystem.order.dto.OrderResponseDto;
import com.karmazyn.logisticsdispatchsystem.order.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper interface for converting between Order entities and DTOs.
 */
@Mapper(componentModel = "spring")
public interface OrderMapper {

    /**
     * Converts an Order entity to an OrderResponseDto.
     * Maps nested driver and user fields to flat DTO properties.
     *
     * @param order the order entity to convert
     * @return the resulting order response DTO
     */
    @Mapping(source = "driver.id", target = "driverId")
    @Mapping(source = "driver.name", target = "driverName")
    @Mapping(source = "createdBy.id", target = "createdByUserId")
    OrderResponseDto toDto(Order order);
}
