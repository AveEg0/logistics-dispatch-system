package com.karmazyn.logisticsdispatchsystem.user.controller;

import com.karmazyn.logisticsdispatchsystem.user.dto.CreateUserRequestDto;
import com.karmazyn.logisticsdispatchsystem.user.dto.UpdatePasswordRequestDto;
import com.karmazyn.logisticsdispatchsystem.user.dto.UserResponseDto;
import com.karmazyn.logisticsdispatchsystem.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing system users.
 * Provides endpoints for user registration, retrieval, and password updates.
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "Operations related to system users")
public class UserController {

    private final UserService userService;

    /**
     * Creates a new user in the system.
     *
     * @param dto the user creation request data
     * @return the created user details
     */
    @PostMapping
    @Operation(summary = "Create a new user", description = "Registers a new user with a specified role (ADMIN, DISPATCHER, DRIVER).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid input or email already exists")
    })
    public UserResponseDto createUser(@Valid @RequestBody CreateUserRequestDto dto) {
        return userService.createUser(dto);
    }

    /**
     * Retrieves a list of all users in the system.
     *
     * @return a list of user details
     */
    @GetMapping
    @Operation(summary = "Get all users", description = "Returns a list of all registered users in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of users")
    })
    public Page<UserResponseDto> getAllUsers(Pageable pageable) {
        return userService.getAllUsers(pageable);
    }

    /**
     * Retrieves user details by their unique identifier.
     *
     * @param id the unique identifier of the user
     * @return the user details
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Returns detailed information about a user based on their unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public UserResponseDto getUserById(
            @Parameter(description = "ID of the user to retrieve", example = "1")
            @PathVariable Long id) {
        return userService.getUserById(id);
    }

    /**
     * Retrieves user details by their email address.
     *
     * @param email the email address of the user
     * @return the user details
     */
    @GetMapping("/search")
    @Operation(summary = "Search user by email", description = "Finds a user in the system using their unique email address.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public UserResponseDto getUserByEmail(
            @Parameter(description = "Email of the user to search for", example = "john.doe@example.com")
            @RequestParam String email) {
        return userService.getUserByEmail(email);
    }

    /**
     * Updates the password for an existing user.
     *
     * @param id the unique identifier of the user
     * @param dto the password update request data
     * @return the updated user details
     */
    @PutMapping("/{id}/password")
    @Operation(summary = "Update user password", description = "Changes the password for the specified user after verifying the old password.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid password or data provided"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public UserResponseDto updatePassword(
            @Parameter(description = "ID of the user to update password for", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody UpdatePasswordRequestDto dto
    ) {
        return userService.updatePassword(id, dto);
    }
}
