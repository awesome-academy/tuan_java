package com.tuanhv.tripgoapi.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ContactRequest(

        @Schema(example = "Nguyễn Văn A")
        @NotBlank(message = "fullName là bắt buộc")
        @Size(max = 255)
        String fullName,

        @Schema(example = "a@example.com")
        @NotBlank(message = "email là bắt buộc")
        @Email(message = "email không hợp lệ")
        String email,

        @Schema(example = "0912345678")
        @NotBlank(message = "phone là bắt buộc")
        @Pattern(
                regexp = "^[0-9+()\\-\\s]{8,20}$",
                message = "phone không hợp lệ"
        )
        String phone
) {
}
