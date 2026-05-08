package com.karmazyn.logisticsdispatchsystem.user.controller;

import com.karmazyn.logisticsdispatchsystem.common.exception.InvalidPrincipalException;
import com.karmazyn.logisticsdispatchsystem.common.exception.UserNotAuthenticatedException;
import com.karmazyn.logisticsdispatchsystem.user.dto.UpdatePasswordRequestDto;
import com.karmazyn.logisticsdispatchsystem.user.dto.UserResponseDto;
import com.karmazyn.logisticsdispatchsystem.user.entity.User;
import com.karmazyn.logisticsdispatchsystem.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void updatePassword_Success() {
        // Given
        UpdatePasswordRequestDto dto = new UpdatePasswordRequestDto();
        dto.setOldPassword("oldPass");
        dto.setNewPassword("newPass");
        User user = new User();
        user.setId(1L);
        UserResponseDto responseDto = new UserResponseDto();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(user);
        when(userService.updatePassword(1L, dto)).thenReturn(responseDto);

        UserResponseDto result = userController.updatePassword(dto);

        // Then
        assertEquals(responseDto, result);
        verify(userService).updatePassword(1L, dto);
    }

    @Test
    void updatePassword_NotAuthenticated_ThrowsException() {
        // Given
        UpdatePasswordRequestDto dto = new UpdatePasswordRequestDto();
        dto.setOldPassword("oldPass");
        dto.setNewPassword("newPass");

        when(securityContext.getAuthentication()).thenReturn(null);

        assertThrows(UserNotAuthenticatedException.class, () -> userController.updatePassword(dto));
        verifyNoInteractions(userService);
    }

    @Test
    void updatePassword_InvalidPrincipal_ThrowsException() {
        // Given
        UpdatePasswordRequestDto dto = new UpdatePasswordRequestDto();
        dto.setOldPassword("oldPass");
        dto.setNewPassword("newPass");

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn("not a user object");

        assertThrows(InvalidPrincipalException.class, () -> userController.updatePassword(dto));
        verifyNoInteractions(userService);
    }
}
