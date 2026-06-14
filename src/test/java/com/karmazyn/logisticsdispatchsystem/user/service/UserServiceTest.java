package com.karmazyn.logisticsdispatchsystem.user.service;

import com.karmazyn.logisticsdispatchsystem.common.exception.EmailAlreadyExistsException;
import com.karmazyn.logisticsdispatchsystem.common.exception.InvalidPasswordException;
import com.karmazyn.logisticsdispatchsystem.common.exception.UserNotFoundException;
import com.karmazyn.logisticsdispatchsystem.driver.repository.DriverRepository;
import com.karmazyn.logisticsdispatchsystem.security.utils.SecurityUtils;
import com.karmazyn.logisticsdispatchsystem.user.dto.CreateUserRequestDto;
import com.karmazyn.logisticsdispatchsystem.user.dto.UpdatePasswordRequestDto;
import com.karmazyn.logisticsdispatchsystem.user.dto.UserResponseDto;
import com.karmazyn.logisticsdispatchsystem.user.entity.User;
import com.karmazyn.logisticsdispatchsystem.user.entity.UserRole;
import com.karmazyn.logisticsdispatchsystem.user.mapper.UserMapper;
import com.karmazyn.logisticsdispatchsystem.user.repository.UserRepository;
import com.karmazyn.logisticsdispatchsystem.user.specification.UserSpecification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private DriverRepository driverRepository;
    @Mock private UserMapper userMapper;
    @Mock private UserSpecification userSpecification;
    @Mock private BCryptPasswordEncoder passwordEncoder;
    @Mock private SecurityUtils securityUtils;

    @InjectMocks
    private UserService userService;

    // createUser

    @Test
    void createUser_Success_UserCreatedWithHashedPassword() {
        // Given
        CreateUserRequestDto dto = new CreateUserRequestDto();
        dto.setEmail("dispatcher@example.com");
        dto.setPassword("SecurePass123!");
        dto.setRole(UserRole.DISPATCHER);

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setEmail("dispatcher@example.com");

        UserResponseDto expected = new UserResponseDto();

        when(userRepository.findByEmail("dispatcher@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("SecurePass123!")).thenReturn("$2a$hashed");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(userMapper.toDto(savedUser)).thenReturn(expected);

        // When
        UserResponseDto result = userService.createUser(dto);

        // Then
        assertEquals(expected, result);
        verify(userRepository).save(argThat(u ->
                "dispatcher@example.com".equals(u.getEmail()) &&
                        "$2a$hashed".equals(u.getPasswordHash())
        ));
    }

    @Test
    void createUser_EmailAlreadyExists_ThrowsEmailAlreadyExistsException() {
        // Given
        CreateUserRequestDto dto = new CreateUserRequestDto();
        dto.setEmail("existing@example.com");
        dto.setPassword("pass");
        dto.setRole(UserRole.DRIVER);

        when(userRepository.findByEmail("existing@example.com"))
                .thenReturn(Optional.of(new User()));

        // When / Then
        assertThrows(EmailAlreadyExistsException.class, () -> userService.createUser(dto));
        verify(userRepository, never()).save(any());
    }

    // updatePassword

    @Test
    void updatePassword_Success_PasswordHashUpdated() {
        // Given
        User user = new User();
        user.setId(1L);
        user.setPasswordHash("$2a$oldHash");

        UpdatePasswordRequestDto dto = new UpdatePasswordRequestDto();
        dto.setOldPassword("oldPass");
        dto.setNewPassword("newSecurePass!");

        UserResponseDto expected = new UserResponseDto();

        when(userRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("oldPass", "$2a$oldHash")).thenReturn(true);
        when(passwordEncoder.encode("newSecurePass!")).thenReturn("$2a$newHash");
        when(userMapper.toDto(user)).thenReturn(expected);

        // When
        UserResponseDto result = userService.updatePassword(1L, dto);

        // Then
        assertEquals("$2a$newHash", user.getPasswordHash());
        assertEquals(expected, result);
    }

    @Test
    void updatePassword_WrongOldPassword_ThrowsInvalidPasswordException() {
        // Given
        User user = new User();
        user.setId(1L);
        user.setPasswordHash("$2a$correctHash");

        UpdatePasswordRequestDto dto = new UpdatePasswordRequestDto();
        dto.setOldPassword("wrongOldPass");
        dto.setNewPassword("newPass");

        when(userRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongOldPass", "$2a$correctHash")).thenReturn(false);

        // When / Then
        assertThrows(InvalidPasswordException.class,
                () -> userService.updatePassword(1L, dto));
    }

    @Test
    void updatePassword_UserNotFound_ThrowsUserNotFoundException() {
        // Given
        when(userRepository.findByIdForUpdate(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(UserNotFoundException.class,
                () -> userService.updatePassword(99L, new UpdatePasswordRequestDto()));
    }
}
