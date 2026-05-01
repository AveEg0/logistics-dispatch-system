package com.karmazyn.logisticsdispatchsystem.common.audit.controller;

import com.karmazyn.logisticsdispatchsystem.common.audit.dto.SecurityLogFilterDto;
import com.karmazyn.logisticsdispatchsystem.common.audit.dto.SecurityLogResponseDto;
import com.karmazyn.logisticsdispatchsystem.common.audit.dto.UserLogFilterDto;
import com.karmazyn.logisticsdispatchsystem.common.audit.entity.SecurityLog;
import com.karmazyn.logisticsdispatchsystem.common.audit.service.SecurityLogService;
import com.karmazyn.logisticsdispatchsystem.common.audit.specification.SecurityLogSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/audit/security-logs")
@RequiredArgsConstructor
public class SecurityLogController {

    private final SecurityLogService service;

    @GetMapping
    public Page<SecurityLogResponseDto> getLogs(
            SecurityLogFilterDto filter,
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable
    ) {
        return service.getLogs(filter, pageable);
    }
}
