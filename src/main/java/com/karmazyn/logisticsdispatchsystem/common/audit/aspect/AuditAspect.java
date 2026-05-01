package com.karmazyn.logisticsdispatchsystem.common.audit.aspect;

import com.karmazyn.logisticsdispatchsystem.common.audit.annotation.AuditAction;
import com.karmazyn.logisticsdispatchsystem.common.audit.service.AuditService;
import com.karmazyn.logisticsdispatchsystem.security.utils.SecurityUtils;
import com.karmazyn.logisticsdispatchsystem.user.entity.User;
import com.karmazyn.logisticsdispatchsystem.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditService auditService;
    private final UserRepository userRepository;
    private final SecurityUtils securityUtils;
    private final String SUCCESS = "SUCCESS";

    @Around("@annotation(auditAction)")
    public Object logAction(ProceedingJoinPoint joinPoint, AuditAction auditAction) throws Throwable {

        HttpServletRequest request = getRequest();

        String ip = request != null ? request.getRemoteAddr() : null;
        String userAgent = request != null ? request.getHeader("User-Agent") : null;
        String uri = request != null ? request.getRequestURI() : null;
        String method = request != null ? request.getMethod() : null;

        String email = securityUtils.getCurrentUserEmail();
        Long userId = null;

        if (email != null) {
            userId = userRepository.findByEmail(email)
                    .map(User::getId)
                    .orElse(null);
        }

        try {
            Object result = joinPoint.proceed();

            auditService.log(
                    auditAction.value(),
                    userId,
                    email,
                    extractEntity(joinPoint),
                    extractEntityId(result),
                    SUCCESS,
                    ip,
                    userAgent,
                    uri,
                    method
            );

            return result;

        } catch (Exception e) {

            auditService.log(
                    auditAction.value(),
                    userId,
                    email,
                    extractEntity(joinPoint),
                    null,
                    e.getMessage(),
                    ip,
                    userAgent,
                    uri,
                    method
            );
            throw e;
        }
    }


    private HttpServletRequest getRequest() {
        ServletRequestAttributes attrs =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        return attrs != null ? attrs.getRequest() : null;
    }

    private String extractEntity(ProceedingJoinPoint joinPoint) {
        return joinPoint.getSignature().getDeclaringTypeName();
    }

    private Long extractEntityId(Object result) {
        try {
            Method method = result.getClass().getMethod("getId");
            return (Long) method.invoke(result);
        } catch (Exception e) {
            return null;
        }
    }

}