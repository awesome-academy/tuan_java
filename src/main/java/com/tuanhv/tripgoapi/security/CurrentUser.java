package com.tuanhv.tripgoapi.security;

public record CurrentUser(
        Long id,
        String email,
        String role
) {
}
