package com.karmazyn.logisticsdispatchsystem.driver.mapper;

import com.karmazyn.logisticsdispatchsystem.driver.dto.DriverResponseDto;
import com.karmazyn.logisticsdispatchsystem.driver.entity.Driver;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper interface for converting between Driver entities and DTOs.
 */
@Mapper(componentModel = "spring")
public interface DriverMapper {

    /**
     * Converts a Driver entity to a DriverResponseDto.
     * Maps the associated user's email to the DTO's email field.
     *
     * @param driver the driver entity to convert
     * @return the resulting driver response DTO
     */
    @Mapping(source = "user.email", target = "email")
    DriverResponseDto toDto(Driver driver);
}
