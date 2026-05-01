package com.karmazyn.logisticsdispatchsystem.common.audit.service;

import com.karmazyn.logisticsdispatchsystem.common.audit.dto.UserLogFilterDto;
import com.karmazyn.logisticsdispatchsystem.common.audit.dto.UserLogResponseDto;
import com.karmazyn.logisticsdispatchsystem.common.audit.entity.UserLog;
import com.karmazyn.logisticsdispatchsystem.common.audit.mapper.UserLogMapper;
import com.karmazyn.logisticsdispatchsystem.common.audit.repository.UserLogRepository;
import com.karmazyn.logisticsdispatchsystem.common.audit.specification.UserLogSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserLogService {

    private final UserLogRepository repository;
    private final UserLogMapper mapper;

    public Page<UserLogResponseDto> getLogs(UserLogFilterDto filter, Pageable pageable) {

        var spec = UserLogSpecification.withFilter(filter);

        return repository.findAll(spec, pageable).map(mapper::toDto);
    }
}
