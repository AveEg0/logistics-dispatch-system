package com.karmazyn.logisticsdispatchsystem.driver.dto;

import com.karmazyn.logisticsdispatchsystem.driver.entity.DriverStatus;
import lombok.Data;

@Data
public class UpdateDriverStatusDto {
    private DriverStatus driverStatus;
}
