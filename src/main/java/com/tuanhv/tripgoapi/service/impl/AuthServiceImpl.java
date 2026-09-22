package com.tuanhv.tripgoapi.service.impl;

import com.tuanhv.tripgoapi.dto.request.LoginRequest;
import com.tuanhv.tripgoapi.dto.request.RegisterRequest;
import com.tuanhv.tripgoapi.dto.response.AuthResponse;
import com.tuanhv.tripgoapi.dto.response.UserResponse;
import com.tuanhv.tripgoapi.entity.User;
import com.tuanhv.tripgoapi.enums.Role;
import com.tuanhv.tripgoapi.exception.BadRequestException;
import com.tuanhv.tripgoapi.exception.ConflictException;
import com.tuanhv.tripgoapi.exception.InvalidCredentialsException;
import com.tuanhv.tripgoapi.exception.ResourceNotFoundException;
import com.tuanhv.tripgoapi.mapper.UserMapper;
import com.tuanhv.tripgoapi.repository.UserRepository;
import com.tuanhv.tripgoapi.security.CurrentUserProvider;
import com.tuanhv.tripgoapi.service.AuthService;
import com.tuanhv.tripgoapi.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final CurrentUserProvider currentUserProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    private final UserMapper userMapper;

    @Override
    public UserResponse register(RegisterRequest request) {
        String email = normalizeEmail(request.email());

        if (userRepository.existsByEmail(email)) {
            throw new ConflictException(
                    "EMAIL_ALREADY_EXISTS",
                    "Email đã được sử dụng"
            );
        }

        User user = new User(
                null,
                request.name().trim(),
                email,
                passwordEncoder.encode(request.password()),
                Role.USER
        );

        try {
            User saved = userRepository.saveAndFlush(user);

            return userMapper.toResponse(saved);
        } catch (DataIntegrityViolationException ex) {
            throw new ConflictException(
                    "EMAIL_ALREADY_EXISTS",
                    "Email đã được sử dụng"
            );
        }
    }

    @Transactional(readOnly = true)
    @Override
    public AuthResponse login(LoginRequest request) {
        String email = normalizeEmail(request.email());

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, request.password())
            );
        } catch (AuthenticationException ex) {
            throw new InvalidCredentialsException("Email hoặc mật khẩu không đúng");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new InvalidCredentialsException("Email hoặc mật khẩu không đúng")
                );

        String token = jwtService.generateToken(user);

        return new AuthResponse(
                token,
                userMapper.toResponse(user)
        );
    }

    @Override
    public UserResponse getCurrentUser() {
        Long userId = currentUserProvider.getUserId();

        if (userId == null) {
            throw new ResourceNotFoundException(
                    "NOT_FOUND",
                    "Không tìm thấy người dùng"
            );
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "NOT_FOUND",
                                "Không tìm thấy người dùng"
                        )
                );

        return userMapper.toResponse(user);
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
