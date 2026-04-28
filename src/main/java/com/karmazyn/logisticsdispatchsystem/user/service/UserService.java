package com.karmazyn.logisticsdispatchsystem.user.service;

import com.karmazyn.logisticsdispatchsystem.common.exception.EmailAlreadyExistsException;
import com.karmazyn.logisticsdispatchsystem.common.exception.UserNotFoundException;
import com.karmazyn.logisticsdispatchsystem.user.dto.CreateUserRequestDto;
import com.karmazyn.logisticsdispatchsystem.user.dto.UpdatePasswordRequestDto;
import com.karmazyn.logisticsdispatchsystem.user.dto.UserResponseDto;
import com.karmazyn.logisticsdispatchsystem.user.entity.User;
import com.karmazyn.logisticsdispatchsystem.user.mapper.UserMapper;
import com.karmazyn.logisticsdispatchsystem.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing system users.
 * Handles business logic for user registration, retrieval, and password security.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * Creates a new user with given role.
     *
     * @param dto The user creation details.
     * @return The created user as a {@link UserResponseDto}.
     * @throws EmailAlreadyExistsException If user with the same email already exists.
     */
    @Transactional
    public UserResponseDto createUser(CreateUserRequestDto dto) {
        // Check for email uniqueness
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("User with email " + dto.getEmail() + " already exists");
        }

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());

        // HASH password
        user.setPasswordHash(
                passwordEncoder.encode(dto.getPassword())
        );

        return userMapper.toDto(userRepository.save(user));
    }

    /**
     * Retrieves all users registered in the system.
     *
     * @return a list of all users as {@link UserResponseDto}
     */
    public Page<UserResponseDto> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::toDto);
    }

    /**
     * Finds user by id.
     *
     * @param id The ID of the user to find.
     * @return The found user as a {@link UserResponseDto}.
     * @throws UserNotFoundException If user is not found.
     */
    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return userMapper.toDto(user);
    }

    /**
     * Finds user by email.
     *
     * @param email The email of the user to find.
     * @return The found user as a {@link UserResponseDto}.
     * @throws UserNotFoundException If user is not found.
     */
    public UserResponseDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));
        return userMapper.toDto(user);
    }

    /**
     * Updates user password.
     * Checks if old password matches before updating.
     *
     * @param id  The ID of the user.
     * @param dto The password update details.
     * @return The updated user as a {@link UserResponseDto}.
     * @throws UserNotFoundException If user is not found.
     * @throws IllegalArgumentException If old password does not match.
     */
    @Transactional
    public UserResponseDto updatePassword(Long id, UpdatePasswordRequestDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Verify old password
        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid old password");
        }

        // Update password hash
        user.setPasswordHash(passwordEncoder.encode(dto.getNewPassword()));
        user.setPasswordChanged(true);

        return userMapper.toDto(user);
    }
}
