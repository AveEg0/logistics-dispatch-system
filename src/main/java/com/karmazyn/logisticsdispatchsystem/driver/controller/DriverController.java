package com.karmazyn.logisticsdispatchsystem.driver.controller;

import com.karmazyn.logisticsdispatchsystem.driver.dto.CreateDriverRequestDto;
import com.karmazyn.logisticsdispatchsystem.driver.dto.*;
import com.karmazyn.logisticsdispatchsystem.driver.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/drivers")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;

    /**
     * Create driver (linked to existing USER with DRIVER role)
     */
    @PostMapping
    public DriverResponseDto createDriver(@RequestBody CreateDriverRequestDto dto) {
        return driverService.createDriver(dto.getName(), dto.getUserId());
    }

    /**
     * Get all drivers (paginated)
     */
    @GetMapping
    public Page<DriverResponseDto> getAllDrivers(Pageable pageable) {
        return driverService.getAllDrivers(pageable);
    }

    /**
     * Get available drivers (paginated)
     */
    @GetMapping("/available")
    public Page<DriverResponseDto> getAvailableDrivers(Pageable pageable) {
        return driverService.getAvailableDrivers(pageable);
    }

    /**
     * Get driver by id
     */
    @GetMapping("/{id}")
    public DriverResponseDto getDriverById(@PathVariable Long id) {
        return driverService.getDriverById(id);
    }

    /**
     * Update driver status
     */
    @PutMapping("/{id}/status")
    public DriverResponseDto updateStatus(
            @PathVariable Long id,
            @RequestBody UpdateDriverStatusDto dto
    ) {
        return driverService.updateDriverStatus(id, dto.getDriverStatus());
    }

    /**
     * Update driver current location
     */
    @PutMapping("/{id}/location")
    public DriverResponseDto updateLocation(
            @PathVariable Long id,
            @RequestBody UpdateDriverCurrentLocationDto dto
    ) {
        return driverService.updateDriverCurrentLocation(id, dto.getCurrentLocation());
    }

    /**
     * Search drivers by name (paginated)
     */
    @GetMapping("/search/name")
    public Page<DriverResponseDto> searchByName(
            @RequestParam String name,
            Pageable pageable
    ) {
        return driverService.findDriversByName(name, pageable);
    }

    /**
     * Search driver by user email
     */
    @GetMapping("/search/email")
    public DriverResponseDto searchByEmail(@RequestParam String email) {
        return driverService.getDriverByUserEmail(email);
    }

}