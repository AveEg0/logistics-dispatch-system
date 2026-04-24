package com.karmazyn.logisticsdispatchsystem.user.service;

import com.karmazyn.logisticsdispatchsystem.common.exception.EmailAlreadyExistsException;
import com.karmazyn.logisticsdispatchsystem.common.exception.UserNotFoundException;
import com.karmazyn.logisticsdispatchsystem.user.dto.CreateUserRequestDto;
import com.karmazyn.logisticsdispatchsystem.user.dto.UserResponseDto;
import com.karmazyn.logisticsdispatchsystem.user.entity.User;
import com.karmazyn.logisticsdispatchsystem.user.mapper.UserMapper;
import com.karmazyn.logisticsdispatchsystem.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * Creates a new user with given role.
     */
    @Transactional
    public UserResponseDto createUser(CreateUserRequestDto dto) {

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
     * Finds user by id.
     */
    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return userMapper.toDto(user);
    }
}
