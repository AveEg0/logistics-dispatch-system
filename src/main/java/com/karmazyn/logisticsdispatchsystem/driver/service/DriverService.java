package com.karmazyn.logisticsdispatchsystem.driver.service;

import com.karmazyn.logisticsdispatchsystem.common.exception.DriverNotFoundException;
import com.karmazyn.logisticsdispatchsystem.common.exception.UserNotFoundException;
import com.karmazyn.logisticsdispatchsystem.common.exception.InvalidUserRoleException;
import com.karmazyn.logisticsdispatchsystem.driver.entity.Driver;
import com.karmazyn.logisticsdispatchsystem.driver.entity.DriverStatus;
import com.karmazyn.logisticsdispatchsystem.driver.repository.DriverRepository;
import com.karmazyn.logisticsdispatchsystem.user.entity.User;
import com.karmazyn.logisticsdispatchsystem.user.entity.UserRole;
import com.karmazyn.logisticsdispatchsystem.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DriverService {

    private final DriverRepository driverRepository;
    private final UserRepository userRepository;

    /**
     * Creates a driver profile for an existing user.
     * User must have DRIVER role.
     */
    @Transactional
    public Driver createDriver(String name, Long userId) {

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

        return driverRepository.save(driver);
    }

    /**
     * Returns all available drivers.
     */
    public List<Driver> getAvailableDrivers() {
        return driverRepository.findByStatus(DriverStatus.AVAILABLE);
    }

    /**
     * Updates driver status (AVAILABLE, BUSY, OFFLINE).
     */
    @Transactional
    public Driver updateStatus(Long driverId, DriverStatus status) {

        Driver driver = driverRepository.findByIdForUpdate(driverId)
                .orElseThrow(() -> new DriverNotFoundException("Driver not found"));

        driver.setStatus(status);

        return driverRepository.save(driver);
    }
}
