package com.karmazyn.logisticsdispatchsystem.driver.dto;

import com.karmazyn.logisticsdispatchsystem.driver.entity.DriverStatus;
import lombok.Data;

@Data
public class DriverFilterDto {
    String name;
    DriverStatus status;
}
