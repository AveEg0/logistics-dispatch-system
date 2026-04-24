package com.karmazyn.logisticsdispatchsystem.driver.service;

import com.karmazyn.logisticsdispatchsystem.common.exception.DriverNotFoundException;
import com.karmazyn.logisticsdispatchsystem.common.exception.UserNotFoundException;
import com.karmazyn.logisticsdispatchsystem.common.exception.InvalidUserRoleException;
import com.karmazyn.logisticsdispatchsystem.driver.dto.DriverResponseDto;
import com.karmazyn.logisticsdispatchsystem.driver.entity.Driver;
import com.karmazyn.logisticsdispatchsystem.driver.entity.DriverStatus;
import com.karmazyn.logisticsdispatchsystem.driver.mapper.DriverMapper;
import com.karmazyn.logisticsdispatchsystem.driver.repository.DriverRepository;
import com.karmazyn.logisticsdispatchsystem.user.entity.User;
import com.karmazyn.logisticsdispatchsystem.user.entity.UserRole;
import com.karmazyn.logisticsdispatchsystem.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DriverService {

    // Repositories
    private final DriverRepository driverRepository;
    private final UserRepository userRepository;
    //Mapper
    private final DriverMapper driverMapper;

    /**
     * Creates a driver profile for an existing user.
     * User must have DRIVER role.
     */
    @Transactional
    public DriverResponseDto createDriver(String name, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Ensure user has DRIVER role
        if (user.getRole() != UserRole.DRIVER) {
            throw new InvalidUserRoleException("User is not a driver");
        }

        Driver driver = new Driver();
        driver.setName(name);
        driver.setStatus(DriverStatus.AVAILABLE);
        driver.setUser(user);

        return driverMapper.toDto(driverRepository.save(driver));
    }

    public DriverResponseDto getDriverById(Long id) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new DriverNotFoundException("Driver not found"));
        return driverMapper.toDto(driver);
    }

    public Page<DriverResponseDto> getAllDrivers(Pageable pageable) {
        return driverRepository.findAll(pageable)
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
     * Returns all available drivers.
     */
    public Page<DriverResponseDto> getAvailableDrivers(Pageable pageable) {
        return driverRepository.findByStatus(DriverStatus.AVAILABLE, pageable)
                .map(driverMapper::toDto);
    }

    /**
     * Updates driver status (AVAILABLE, BUSY, OFFLINE).
     */
    @Transactional
    public DriverResponseDto updateDriverStatus(Long driverId, DriverStatus status) {

        Driver driver = driverRepository.findByIdForUpdate(driverId)
                .orElseThrow(() -> new DriverNotFoundException("Driver not found"));

        driver.setStatus(status);

        return driverMapper.toDto(driverRepository.save(driver));
    }

    @Transactional
    public DriverResponseDto updateDriverCurrentLocation(Long driverId, String location) {
        Driver driver = driverRepository.findByIdForUpdate(driverId)
                .orElseThrow(() -> new DriverNotFoundException("Driver not found"));

        driver.setCurrentLocation(location);

        return driverMapper.toDto(driverRepository.save(driver));
    }
}
