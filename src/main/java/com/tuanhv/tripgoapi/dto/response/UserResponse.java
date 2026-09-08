package com.tuanhv.tripgoapi.dto.response;

public record UserResponse(
        Long id,
        String name,
        String email,
        String role
) {
}
