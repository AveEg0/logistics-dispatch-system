package com.karmazyn.logisticsdispatchsystem.driver.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateDriverRequestDto {
    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "User ID is required")
    private Long userId;
}
