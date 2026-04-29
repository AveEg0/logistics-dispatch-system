package com.karmazyn.logisticsdispatchsystem.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String AccessToken;
    private String RefreshToken;
}
