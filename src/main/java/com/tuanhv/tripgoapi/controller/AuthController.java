package com.tuanhv.tripgoapi.controller;

import com.tuanhv.tripgoapi.dto.request.LoginRequest;
import com.tuanhv.tripgoapi.dto.request.RegisterRequest;
import com.tuanhv.tripgoapi.dto.response.AuthResponse;
import com.tuanhv.tripgoapi.dto.response.ErrorResponse;
import com.tuanhv.tripgoapi.dto.response.UserResponse;
import com.tuanhv.tripgoapi.dto.response.ValidationErrorResponse;
import com.tuanhv.tripgoapi.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "01. Authentication")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Đăng ký tài khoản",
            description = """
                Tạo tài khoản người dùng mới.
                Password được hash bằng BCrypt trước khi lưu.
                """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Đăng ký thành công"
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Dữ liệu đầu vào không hợp lệ",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ValidationErrorResponse.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Email đã tồn tại",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ErrorResponse.class
                            )
                    )
            )
    })
    public UserResponse register(
            @Valid @RequestBody RegisterRequest request
    ) {
        return authService.register(request);
    }

    @PostMapping("/login")
    @Operation(
            summary = "Đăng nhập",
            description = "Xác thực email/password và trả JWT"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Đăng nhập thành công"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Email hoặc mật khẩu không đúng"
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "Request không hợp lệ",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    implementation = ValidationErrorResponse.class
                            )
                    )
            )
    })
    public AuthResponse login(
            @Valid @RequestBody LoginRequest request
    ) {
        return authService.login(request);
    }

    @GetMapping("/me")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(
            summary = "Thông tin người dùng hiện tại"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lấy user thành công"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "JWT thiếu, sai hoặc hết hạn"
            )
    })
    public UserResponse me(
            @AuthenticationPrincipal Jwt jwt
    ) {
        return authService.getCurrentUser(jwt);
    }

}
