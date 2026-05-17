package com.karmazyn.logisticsdispatchsystem.user.controller;

import com.karmazyn.logisticsdispatchsystem.common.audit.annotation.AuditAction;
import com.karmazyn.logisticsdispatchsystem.common.audit.entity.UserAction;
import com.karmazyn.logisticsdispatchsystem.common.exception.InvalidPrincipalException;
import com.karmazyn.logisticsdispatchsystem.common.exception.UserNotAuthenticatedException;
import com.karmazyn.logisticsdispatchsystem.user.dto.*;
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
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.karmazyn.logisticsdispatchsystem.user.entity.User;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for managing system users.
 * Provides endpoints for user registration, retrieval, and password updates.
 */
@PreAuthorize("hasRole('ADMIN')")
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
    @AuditAction(UserAction.CREATE_USER)
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
     * Retrieves a list of users in the system.
     *
     * @return a list of user details
     */
    @GetMapping
    @Operation(summary = "Get users", description = "Returns a list of registered users in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of users")
    })
    public Page<UserResponseDto> getUsers(
            UserFilterDto filter,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        return userService.getUsers(filter, pageable);
    }

    @GetMapping("/me")
    public MeResponseDto getMe() {
        return userService.getCurrentUser();
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
     * Updates the password for the currently authenticated user.
     *
     * @param dto the password update request data
     * @return the updated user details
     */
    @AuditAction(UserAction.UPDATE_USER_PASSWORD)
    @PutMapping("/me/password")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Update my password", description = "Changes the password for the currently authenticated user after verifying the old password.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid password or data provided"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public UserResponseDto updatePassword(
            @Valid @RequestBody UpdatePasswordRequestDto dto
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UserNotAuthenticatedException("User not authenticated");
        }

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof User user)) {
            throw new InvalidPrincipalException("Invalid principal");
        }
        return userService.updatePassword(user.getId(), dto);
    }
}
