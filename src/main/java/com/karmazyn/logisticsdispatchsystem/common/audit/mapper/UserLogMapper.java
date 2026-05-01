package com.karmazyn.logisticsdispatchsystem.common.audit.mapper;

import com.karmazyn.logisticsdispatchsystem.common.audit.dto.UserLogResponseDto;
import com.karmazyn.logisticsdispatchsystem.common.audit.entity.UserLog;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserLogMapper {

    UserLogResponseDto toDto(UserLog userLog);
}
