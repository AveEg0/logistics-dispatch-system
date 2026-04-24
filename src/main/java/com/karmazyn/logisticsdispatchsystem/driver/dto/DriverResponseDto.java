package com.karmazyn.logisticsdispatchsystem.driver.dto;

import com.karmazyn.logisticsdispatchsystem.driver.entity.DriverStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DriverResponseDto {
    private Long id;
    private String name;
    private DriverStatus status;
    private String currentLocation;
    private String email;
}
