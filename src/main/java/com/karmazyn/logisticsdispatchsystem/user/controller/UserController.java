package com.karmazyn.logisticsdispatchsystem.user.controller;

import com.karmazyn.logisticsdispatchsystem.user.dto.CreateUserRequestDto;
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

    @PostMapping
    public UserResponseDto createUser(@Valid @RequestBody CreateUserRequestDto dto) {
        return userService.createUser(dto);
    }

    @GetMapping("/{id}")
    public UserResponseDto getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }
}
