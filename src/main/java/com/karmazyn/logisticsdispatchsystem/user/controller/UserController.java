package com.karmazyn.logisticsdispatchsystem.user.controller;

import com.karmazyn.logisticsdispatchsystem.user.dto.CreateUserRequestDto;
import com.karmazyn.logisticsdispatchsystem.user.dto.UpdatePasswordRequestDto;
import com.karmazyn.logisticsdispatchsystem.user.dto.UserResponseDto;
import com.karmazyn.logisticsdispatchsystem.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Create a new user
     */
    @PostMapping
    public UserResponseDto createUser(@Valid @RequestBody CreateUserRequestDto dto) {
        return userService.createUser(dto);
    }

    /**
     * Get user by id
     */
    @GetMapping("/{id}")
    public UserResponseDto getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    /**
     * Get user by email
     */
    @GetMapping("/search")
    public UserResponseDto getUserByEmail(@RequestParam String email) {
        return userService.getUserByEmail(email);
    }

    /**
     * Update user password
     */
    @PutMapping("/{id}/password")
    public UserResponseDto updatePassword(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePasswordRequestDto dto
    ) {
        return userService.updatePassword(id, dto);
    }
}
