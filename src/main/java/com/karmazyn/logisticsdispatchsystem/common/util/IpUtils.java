package com.karmazyn.logisticsdispatchsystem.common.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class IpUtils {

    public String resolveIp(HttpServletRequest request) {

        if (request == null) return null;

        String forwarded = request.getHeader("X-Forwarded-For");

        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }

        return request.getRemoteAddr();
    }
}
