package com.karmazyn.logisticsdispatchsystem.user.mapper;

import com.karmazyn.logisticsdispatchsystem.user.dto.UserResponseDto;
import com.karmazyn.logisticsdispatchsystem.user.entity.User;
import org.mapstruct.Mapper;

/**
 * Mapper interface for converting between User entities and DTOs.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {
    /**
     * Converts a User entity to a UserResponseDto.
     *
     * @param user the user entity to convert
     * @return the resulting user response DTO
     */
    UserResponseDto toDto(User user);
}
