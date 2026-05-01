package com.karmazyn.logisticsdispatchsystem.security.handler;

import com.karmazyn.logisticsdispatchsystem.common.audit.entity.SecurityAction;
import com.karmazyn.logisticsdispatchsystem.common.audit.service.SecurityLogService;
import com.karmazyn.logisticsdispatchsystem.security.utils.SecurityUtils;
import com.karmazyn.logisticsdispatchsystem.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class SecurityExceptionHandler {

    private final SecurityLogService securityLogService;
    private final SecurityUtils securityUtils;

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<String> handleAccessDenied(
            AuthorizationDeniedException ex,
            HttpServletRequest request
    ) {

        User user = null;

        try {
            user = securityUtils.getCurrentUser();
        } catch (Exception ignored) {}

        securityLogService.log(
                securityLogService.buildLog(
                        SecurityAction.ACCESS_DENIED,
                        user != null ? user.getId() : null,
                        user != null ? user.getEmail() : null,
                        false,
                        request,
                        ex.getMessage()
                )
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Access Denied");
    }
}