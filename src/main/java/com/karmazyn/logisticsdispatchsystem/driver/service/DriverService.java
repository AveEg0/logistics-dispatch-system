package com.karmazyn.logisticsdispatchsystem.driver.service;

import com.karmazyn.logisticsdispatchsystem.common.exception.DriverNotFoundException;
import com.karmazyn.logisticsdispatchsystem.common.exception.UserNotFoundException;
import com.karmazyn.logisticsdispatchsystem.common.exception.InvalidUserRoleException;
import com.karmazyn.logisticsdispatchsystem.driver.dto.*;
import com.karmazyn.logisticsdispatchsystem.driver.entity.Driver;
import com.karmazyn.logisticsdispatchsystem.driver.entity.DriverStatus;
import com.karmazyn.logisticsdispatchsystem.driver.mapper.DriverMapper;
import com.karmazyn.logisticsdispatchsystem.driver.repository.DriverRepository;
import com.karmazyn.logisticsdispatchsystem.driver.specification.DriverSpecification;
import com.karmazyn.logisticsdispatchsystem.user.entity.User;
import com.karmazyn.logisticsdispatchsystem.user.entity.UserRole;
import com.karmazyn.logisticsdispatchsystem.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * Service class for managing driver profiles.
 * Handles business logic for driver registration, status updates, and location tracking.
 */
@Service
@RequiredArgsConstructor
public class DriverService {

    // Repositories
    private final DriverRepository driverRepository;
    private final UserRepository userRepository;
    //Mapper
    private final DriverMapper driverMapper;
    private final DriverSpecification driverSpecification;

    /**
     * Creates a driver profile for an existing user.
     * User must have DRIVER role.
     *
     * @param dto the driver creation request data
     * @return the created driver details
     * @throws UserNotFoundException if the user does not exist
     * @throws InvalidUserRoleException if the user does not have the DRIVER role
     */
    @Transactional
    public DriverResponseDto createDriver(CreateDriverRequestDto dto) {

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Ensure user has DRIVER role
        if (user.getRole() != UserRole.DRIVER) {
            throw new InvalidUserRoleException("User is not a driver");
        }

        Driver driver = new Driver();
        driver.setName(dto.getName());
        driver.setStatus(DriverStatus.AVAILABLE);
        driver.setUser(user);

        return driverMapper.toDto(driverRepository.save(driver));
    }

    public DriverResponseDto getDriverById(Long id) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new DriverNotFoundException("Driver not found"));
        return driverMapper.toDto(driver);
    }

    public Page<DriverResponseDto> getDrivers(DriverFilterDto filter, Pageable pageable) {
        Specification<Driver> specification = driverSpecification.withFilter(filter);
        return driverRepository.findAll(specification, pageable)
                .map(driverMapper::toDto);
    }

    public Page<DriverResponseDto> findDriversByName(String name, Pageable pageable) {
        return driverRepository.findByNameContainingIgnoreCase(name, pageable)
                .map(driverMapper::toDto);
    }

    public DriverResponseDto getDriverByUserEmail(String email) {
        Driver driver = driverRepository.findByUserEmail(email)
                .orElseThrow(() -> new DriverNotFoundException("Driver not found"));

        return driverMapper.toDto(driver);
    }

    /**
     * Updates driver status (AVAILABLE, BUSY, OFFLINE, RESERVED).
     */
    @Transactional
    public DriverResponseDto updateDriverStatus(Long driverId, UpdateDriverStatusDto dto) {

        Driver driver = driverRepository.findByIdForUpdate(driverId)
                .orElseThrow(() -> new DriverNotFoundException("Driver not found"));

        driver.setStatus(dto.getDriverStatus());

        return driverMapper.toDto(driver);
    }

    @Transactional
    public DriverResponseDto updateDriverCurrentLocation(Long driverId, UpdateDriverCurrentLocationDto dto) {
        Driver driver = driverRepository.findByIdForUpdate(driverId)
                .orElseThrow(() -> new DriverNotFoundException("Driver not found"));

        driver.setCurrentLocation(dto.getCurrentLocation());

        return driverMapper.toDto(driver);
    }
}
