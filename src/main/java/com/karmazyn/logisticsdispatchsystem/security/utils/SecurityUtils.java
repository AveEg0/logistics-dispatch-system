package com.karmazyn.logisticsdispatchsystem.security.utils;

import com.karmazyn.logisticsdispatchsystem.common.exception.InvalidPrincipalException;
import com.karmazyn.logisticsdispatchsystem.common.exception.UserNotAuthenticatedException;
import com.karmazyn.logisticsdispatchsystem.user.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {
    private final String ANONYMOUS = "anonymous user";

    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new UserNotAuthenticatedException("User not authenticated");
        }

        Object principal = auth.getPrincipal();

        if (principal instanceof User user) {
            return user;
        }

        throw new InvalidPrincipalException("Invalid principal");
    }

    public String getCurrentUserEmail() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName() : ANONYMOUS;
    }
}
