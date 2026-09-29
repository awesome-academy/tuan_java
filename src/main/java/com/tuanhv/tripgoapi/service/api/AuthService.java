package com.tuanhv.tripgoapi.service.api;

import com.tuanhv.tripgoapi.dto.request.LoginRequest;
import com.tuanhv.tripgoapi.dto.request.RegisterRequest;
import com.tuanhv.tripgoapi.dto.response.AuthResponse;
import com.tuanhv.tripgoapi.dto.response.UserResponse;

public interface AuthService {

    UserResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    UserResponse getCurrentUser();
}
