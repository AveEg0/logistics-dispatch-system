package com.karmazyn.logisticsdispatchsystem.security.handler;

import com.karmazyn.logisticsdispatchsystem.common.audit.entity.SecurityAction;
import com.karmazyn.logisticsdispatchsystem.common.audit.service.SecurityLogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final SecurityLogService securityLogService;
    private final String UNAUTHORIZED = "Unauthorized";

    @Override
    public void commence(@NonNull HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        securityLogService.log(
                securityLogService.buildLog(
                        SecurityAction.UNAUTHORIZED,
                        null,
                        null,
                        false,
                        request,
                        UNAUTHORIZED +": "+ authException.getMessage()
                )
        );

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(UNAUTHORIZED);
    }
}
