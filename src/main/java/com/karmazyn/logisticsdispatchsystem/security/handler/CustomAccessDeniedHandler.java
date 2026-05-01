package com.karmazyn.logisticsdispatchsystem.security.handler;

import com.karmazyn.logisticsdispatchsystem.common.audit.entity.SecurityAction;
import com.karmazyn.logisticsdispatchsystem.common.audit.service.SecurityLogService;
import com.karmazyn.logisticsdispatchsystem.security.utils.SecurityUtils;
import com.karmazyn.logisticsdispatchsystem.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final SecurityLogService securityLogService;
    private final SecurityUtils securityUtils;

    private static final String ACCESS_DENIED = "Access Denied";

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException exception) throws IOException {

        User user = null;

        try {
            user = securityUtils.getCurrentUser();
        } catch (Exception ignored) {
            // user may not exist for anonymous requests
        }

        securityLogService.log(
                securityLogService.buildLog(
                        SecurityAction.ACCESS_DENIED,
                        user != null ? user.getId() : null,
                        user != null ? user.getEmail() : null,
                        false,
                        request,
                        ACCESS_DENIED + ": " + exception.getMessage()
                )
        );

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write(ACCESS_DENIED);
    }
}
