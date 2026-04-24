package com.karmazyn.logisticsdispatchsystem.driver.mapper;

import com.karmazyn.logisticsdispatchsystem.driver.dto.DriverResponseDto;
import com.karmazyn.logisticsdispatchsystem.driver.entity.Driver;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DriverMapper {

    @Mapping(source = "user.email", target = "email")
    DriverResponseDto toDto(Driver driver);
}
