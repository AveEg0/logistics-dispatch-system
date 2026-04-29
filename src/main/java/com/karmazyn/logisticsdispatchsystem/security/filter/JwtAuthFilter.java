package com.karmazyn.logisticsdispatchsystem.security.filter;

import com.karmazyn.logisticsdispatchsystem.common.exception.JwtAuthenticationException;
import com.karmazyn.logisticsdispatchsystem.security.service.JwtAuthService;
import com.karmazyn.logisticsdispatchsystem.security.service.JwtService;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {



    private final JwtService jwtService;
    private final JwtAuthService jwtAuthService;

    @Override
    protected void doFilterInternal(
            @Nonnull HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
        Optional<String> jwt = jwtAuthService.extractToken(request);

        if (jwt.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = jwt.get();

        if (!jwtService.isTokenValid(token)) {
            filterChain.doFilter(request, response);
            return;
        }


            String email = jwtService.extractEmail(token);
            List<String> roles = jwtService.extractRoles(token);

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                List<GrantedAuthority> authorities = roles.stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .collect(Collectors.toList());
                log.info("Authorities: {}", authorities);
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                authorities
                        );
                SecurityContextHolder.getContext().setAuthentication(auth);
                log.info("Authorities: {}", auth.getAuthorities());
            }
        } catch (JwtAuthenticationException e) {
            log.error("Authentication failed: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Cannot set user authentication: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

}
