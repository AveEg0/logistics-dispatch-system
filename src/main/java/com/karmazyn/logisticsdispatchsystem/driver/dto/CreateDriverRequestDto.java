package com.karmazyn.logisticsdispatchsystem.driver.dto;

import lombok.Data;

@Data
public class CreateDriverRequestDto {
    private String name;
    private Long userId;
}
