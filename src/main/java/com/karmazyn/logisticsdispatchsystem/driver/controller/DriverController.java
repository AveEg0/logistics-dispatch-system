package com.karmazyn.logisticsdispatchsystem.driver.controller;

import com.karmazyn.logisticsdispatchsystem.common.audit.annotation.AuditAction;
import com.karmazyn.logisticsdispatchsystem.common.audit.entity.UserAction;
import com.karmazyn.logisticsdispatchsystem.driver.dto.CreateDriverRequestDto;
import com.karmazyn.logisticsdispatchsystem.driver.dto.*;
import com.karmazyn.logisticsdispatchsystem.driver.service.DriverService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for managing driver profiles and their availability.
 * Provides endpoints for driver registration, status updates, and location tracking.
 */
@PreAuthorize("hasAnyRole('ADMIN', 'DISPATCHER')")
@RestController
@RequestMapping("/drivers")
@RequiredArgsConstructor
@Tag(name = "Driver Management", description = "Operations related to driver profiles and availability")
public class DriverController {

    private final DriverService driverService;

    /**
     * Creates a new driver profile linked to an existing user.
     *
     * @param dto the driver creation request data
     * @return the created driver details
     */
    @PreAuthorize("hasRole('ADMIN')")
    @AuditAction(UserAction.CREATE_DRIVER)
    @PostMapping
    @Operation(summary = "Create driver profile", description = "Links a new driver profile to an existing user with the DRIVER role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Driver profile created"),
            @ApiResponse(responseCode = "400", description = "Invalid input or user already has a driver profile"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public DriverResponseDto createDriver(@Valid @RequestBody CreateDriverRequestDto dto) {
        return driverService.createDriver(dto);
    }

    /**
     * Retrieves a paginated list of drivers.
     *
     * @param pageable pagination and sorting information
     * @return a page of driver details
     */
    @GetMapping
    @Operation(summary = "Get drivers", description = "Returns a paginated list of registered drivers.")
    public Page<DriverResponseDto> getDrivers(
            DriverFilterDto filter,
            @Parameter(description = "Pagination and sorting information")
            @PageableDefault(size = 20, sort = "name", direction = Sort.Direction.DESC)
            Pageable pageable) {
        return driverService.getDrivers(filter, pageable);
    }

    /**
     * Retrieves driver details by their unique identifier.
     *
     * @param id the unique identifier of the driver
     * @return the driver details
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get driver by ID", description = "Returns detailed information about a driver based on their unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Driver found"),
            @ApiResponse(responseCode = "404", description = "Driver not found")
    })
    public DriverResponseDto getDriverById(
            @Parameter(description = "ID of the driver to retrieve", example = "1")
            @PathVariable Long id) {
        return driverService.getDriverById(id);
    }

    /**
     * Updates the availability status of a driver.
     *
     * @param id the unique identifier of the driver
     * @param dto the status update request data
     * @return the updated driver details
     */
    @AuditAction(UserAction.UPDATE_DRIVER_STATUS)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/status")
    @Operation(summary = "Update driver status", description = "Updates the current availability status (e.g., AVAILABLE, BUSY, OFFLINE) of a driver.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status updated"),
            @ApiResponse(responseCode = "404", description = "Driver not found")
    })
    public DriverResponseDto updateStatus(
            @Parameter(description = "ID of the driver to update", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody UpdateDriverStatusDto dto
    ) {
        return driverService.updateDriverStatus(id, dto);
    }

    /**
     * Updates the current location coordinates or address of a driver.
     *
     * @param id the unique identifier of the driver
     * @param dto the location update request data
     * @return the updated driver details
     */

    @AuditAction(UserAction.UPDATE_DRIVER_LOCATION)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/location")
    @Operation(summary = "Update driver location", description = "Updates the real-time location coordinates or address of a driver.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Location updated"),
            @ApiResponse(responseCode = "404", description = "Driver not found")
    })
    public DriverResponseDto updateLocation(
            @Parameter(description = "ID of the driver to update", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody UpdateDriverCurrentLocationDto dto
    ) {
        return driverService.updateDriverCurrentLocation(id, dto);
    }

    /**
     * Searches for drivers by their name.
     *
     * @param name the name (or partial name) to search for
     * @param pageable pagination and sorting information
     * @return a page of matching driver details
     */
    @GetMapping("/search/name")
    @Operation(summary = "Search drivers by name", description = "Returns a paginated list of drivers whose names match the search criteria.")
    public Page<DriverResponseDto> searchByName(
            @Parameter(description = "Name or partial name of the driver", example = "Michael")
            @RequestParam String name,
            @Parameter(description = "Pagination and sorting information") Pageable pageable
    ) {
        return driverService.findDriversByName(name, pageable);
    }

    /**
     * Retrieves driver details by the email of their associated user account.
     *
     * @param email the email address of the user
     * @return the driver details
     */
    @GetMapping("/search/email")
    @Operation(summary = "Get driver by user email", description = "Finds a driver profile using the unique email of the associated user account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Driver found"),
            @ApiResponse(responseCode = "404", description = "Driver not found")
    })
    public DriverResponseDto searchByEmail(
            @Parameter(description = "Email of the associated user", example = "michael.smith@example.com")
            @RequestParam String email) {
        return driverService.getDriverByUserEmail(email);
    }
}