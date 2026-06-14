package com.karmazyn.logisticsdispatchsystem.driver.service;

import com.karmazyn.logisticsdispatchsystem.common.exception.DriverNotFoundException;
import com.karmazyn.logisticsdispatchsystem.common.exception.InvalidUserRoleException;
import com.karmazyn.logisticsdispatchsystem.common.exception.UserNotFoundException;
import com.karmazyn.logisticsdispatchsystem.driver.dto.CreateDriverRequestDto;
import com.karmazyn.logisticsdispatchsystem.driver.dto.DriverResponseDto;
import com.karmazyn.logisticsdispatchsystem.driver.dto.UpdateDriverStatusDto;
import com.karmazyn.logisticsdispatchsystem.driver.entity.Driver;
import com.karmazyn.logisticsdispatchsystem.driver.entity.DriverStatus;
import com.karmazyn.logisticsdispatchsystem.driver.mapper.DriverMapper;
import com.karmazyn.logisticsdispatchsystem.driver.repository.DriverRepository;
import com.karmazyn.logisticsdispatchsystem.driver.specification.DriverSpecification;
import com.karmazyn.logisticsdispatchsystem.user.entity.User;
import com.karmazyn.logisticsdispatchsystem.user.entity.UserRole;
import com.karmazyn.logisticsdispatchsystem.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DriverServiceTest {

    @Mock private DriverRepository driverRepository;
    @Mock private UserRepository userRepository;
    @Mock private DriverMapper driverMapper;
    @Mock private DriverSpecification driverSpecification;

    @InjectMocks
    private DriverService driverService;

    // reateDriver

    @Test
    void createDriver_Success_DriverCreatedWithAvailableStatus() {
        // Given
        User user = new User();
        user.setId(1L);
        user.setRole(UserRole.DRIVER);

        CreateDriverRequestDto dto = new CreateDriverRequestDto();
        dto.setUserId(1L);
        dto.setName("John Doe");

        Driver savedDriver = new Driver();
        savedDriver.setName("John Doe");
        savedDriver.setStatus(DriverStatus.AVAILABLE);
        savedDriver.setUser(user);

        DriverResponseDto expected = new DriverResponseDto();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(driverRepository.save(any(Driver.class))).thenReturn(savedDriver);
        when(driverMapper.toDto(savedDriver)).thenReturn(expected);

        // When
        DriverResponseDto result = driverService.createDriver(dto);

        // Then
        assertEquals(expected, result);
        verify(driverRepository).save(argThat(d ->
                "John Doe".equals(d.getName()) &&
                        d.getStatus() == DriverStatus.AVAILABLE
        ));
    }

    @Test
    void createDriver_UserNotFound_ThrowsUserNotFoundException() {
        // Given
        CreateDriverRequestDto dto = new CreateDriverRequestDto();
        dto.setUserId(99L);

        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(UserNotFoundException.class, () -> driverService.createDriver(dto));
        verifyNoInteractions(driverRepository);
    }

    @Test
    void createDriver_UserNotDriverRole_ThrowsInvalidUserRoleException() {
        // Given
        User user = new User();
        user.setId(1L);
        user.setRole(UserRole.DISPATCHER); // wrong role

        CreateDriverRequestDto dto = new CreateDriverRequestDto();
        dto.setUserId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // When / Then
        assertThrows(InvalidUserRoleException.class, () -> driverService.createDriver(dto));
        verifyNoInteractions(driverRepository);
    }

    // updateDriverStatus

    @Test
    void updateDriverStatus_Success_StatusUpdated() {
        // Given
        Driver driver = new Driver();
        driver.setId(1L);
        driver.setStatus(DriverStatus.AVAILABLE);

        UpdateDriverStatusDto dto = new UpdateDriverStatusDto();
        dto.setDriverStatus(DriverStatus.OFFLINE);

        DriverResponseDto expected = new DriverResponseDto();

        when(driverRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(driver));
        when(driverRepository.save(driver)).thenReturn(driver);
        when(driverMapper.toDto(driver)).thenReturn(expected);

        // When
        DriverResponseDto result = driverService.updateDriverStatus(1L, dto);

        // Then
        assertEquals(DriverStatus.OFFLINE, driver.getStatus());
        assertEquals(expected, result);
    }

    @Test
    void updateDriverStatus_DriverNotFound_ThrowsDriverNotFoundException() {
        // Given
        when(driverRepository.findByIdForUpdate(99L)).thenReturn(Optional.empty());

        UpdateDriverStatusDto dto = new UpdateDriverStatusDto();
        dto.setDriverStatus(DriverStatus.OFFLINE);

        // When / Then
        assertThrows(DriverNotFoundException.class,
                () -> driverService.updateDriverStatus(99L, dto));
    }
}