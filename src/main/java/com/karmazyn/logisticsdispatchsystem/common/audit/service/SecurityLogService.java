package com.karmazyn.logisticsdispatchsystem.common.audit.service;

import com.karmazyn.logisticsdispatchsystem.common.audit.dto.SecurityLogFilterDto;
import com.karmazyn.logisticsdispatchsystem.common.audit.dto.SecurityLogRequestDto;
import com.karmazyn.logisticsdispatchsystem.common.audit.dto.SecurityLogResponseDto;
import com.karmazyn.logisticsdispatchsystem.common.audit.entity.SecurityAction;
import com.karmazyn.logisticsdispatchsystem.common.audit.mapper.SecurityLogMapper;
import com.karmazyn.logisticsdispatchsystem.common.audit.repository.SecurityLogRepository;
import com.karmazyn.logisticsdispatchsystem.common.audit.specification.SecurityLogSpecification;
import com.karmazyn.logisticsdispatchsystem.common.util.IpUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SecurityLogService {

    private final SecurityLogRepository repository;
    private final SecurityLogMapper mapper;
    private final IpUtils ipUtils;

    public Page<SecurityLogResponseDto> getLogs(SecurityLogFilterDto filter, Pageable pageable) {

        var spec = SecurityLogSpecification.withFilter(filter);

        return repository.findAll(spec, pageable).map(mapper::toDto);
    }

    @Async
    public void log(SecurityLogRequestDto request) {
        repository.save(mapper.toEntity(request));
    }

    public SecurityLogRequestDto buildLog(
            SecurityAction action,
            Long userId,
            String email,
            boolean success,
            HttpServletRequest request,
            String details
    ) {

        return new SecurityLogRequestDto(
                action,
                userId,
                email,
                success,
                request != null ? ipUtils.resolveIp(request) : null,
                request != null ? request.getHeader("User-Agent") : null,
                request != null ? request.getRequestURI() : null,
                request != null ? request.getMethod() : null,
                details
        );
    }

}
