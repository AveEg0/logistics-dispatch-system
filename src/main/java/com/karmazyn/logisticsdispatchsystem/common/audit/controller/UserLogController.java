package com.karmazyn.logisticsdispatchsystem.common.audit.controller;

import com.karmazyn.logisticsdispatchsystem.common.audit.dto.UserLogFilterDto;
import com.karmazyn.logisticsdispatchsystem.common.audit.dto.UserLogResponseDto;
import com.karmazyn.logisticsdispatchsystem.common.audit.entity.UserLog;
import com.karmazyn.logisticsdispatchsystem.common.audit.service.UserLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@PreAuthorize( "hasAnyRole('ADMIN')")
@RestController
@RequestMapping("/audit/user-logs")
@RequiredArgsConstructor
public class UserLogController {

    private final UserLogService service;

    @GetMapping
    public Page<UserLogResponseDto> getLogs(
            UserLogFilterDto filter,
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable
    ) {
        return service.getLogs(filter, pageable);
    }
}