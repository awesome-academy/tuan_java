package com.tuanhv.tripgoapi.dto.response;

public record ContactResponse(
        String fullName,
        String email,
        String phone
) {
}
