package com.karmazyn.logisticsdispatchsystem.order.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CompleteOrderRequestDto {
    @Size(max = 500, message = "Comment must not exceed 500 characters")
    private String comment;
}
