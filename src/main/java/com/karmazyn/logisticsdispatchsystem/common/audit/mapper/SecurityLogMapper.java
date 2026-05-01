package com.karmazyn.logisticsdispatchsystem.common.audit.mapper;

import com.karmazyn.logisticsdispatchsystem.common.audit.dto.SecurityLogRequestDto;
import com.karmazyn.logisticsdispatchsystem.common.audit.dto.SecurityLogResponseDto;
import com.karmazyn.logisticsdispatchsystem.common.audit.entity.SecurityLog;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SecurityLogMapper {
    SecurityLogResponseDto toDto(SecurityLog securityLog);
    SecurityLog toEntity(SecurityLogRequestDto securityLogResponseDto);
}
