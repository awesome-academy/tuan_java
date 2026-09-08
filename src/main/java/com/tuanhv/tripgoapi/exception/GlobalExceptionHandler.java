package com.tuanhv.tripgoapi.exception;

import com.tuanhv.tripgoapi.dto.response.ErrorResponse;
import com.tuanhv.tripgoapi.dto.response.ValidationErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex
    ) {
        ErrorResponse response = new ErrorResponse(
                new ErrorResponse.ErrorDetail(
                        "INVALID_PARAMETER",
                        "Invalid value for parameter: " + ex.getName()
                )
        );

        return ResponseEntity
                .badRequest()
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> fields = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(
                        Collectors.toMap(
                                FieldError::getField,
                                fieldError ->
                                        Objects.requireNonNullElse(
                                                fieldError.getDefaultMessage(),
                                                "Giá trị không hợp lệ"
                                        ),
                                (first, second) -> first,
                                LinkedHashMap::new
                        )
                );

        ValidationErrorResponse response = new ValidationErrorResponse(
                new ValidationErrorResponse.ValidationError(
                        "VALIDATION_ERROR",
                        "Dữ liệu không hợp lệ",
                        fields
                )
        );

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_CONTENT)
                .body(response);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(
            BadRequestException ex
    ) {
        ErrorResponse response = new ErrorResponse(
                new ErrorResponse.ErrorDetail(
                        ex.getCode(),
                        ex.getMessage()
                )
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
            ResourceNotFoundException ex
    ) {
        ErrorResponse response = new ErrorResponse(
                new ErrorResponse.ErrorDetail(
                        ex.getCode(),
                        ex.getMessage()
                )
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(
            Exception ex
    ) {
        log.error("Unhandled exception", ex);

        ErrorResponse response = new ErrorResponse(
                new ErrorResponse.ErrorDetail(
                        "INTERNAL_SERVER_ERROR",
                        "An unexpected error occurred"
                )
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentials(InvalidCredentialsException ex) {
        ErrorResponse response = new ErrorResponse(
                new ErrorResponse.ErrorDetail(
                        "INVALID_CREDENTIALS",
                        ex.getMessage()
                )
        );

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }

    @ExceptionHandler(BookingConflictException.class)
    public ResponseEntity<ErrorResponse> handleBookingConflict(BookingConflictException ex) {
        ErrorResponse response = new ErrorResponse(
                new ErrorResponse.ErrorDetail(
                        ex.getCode(),
                        ex.getMessage()
                )
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbidden(ForbiddenException ex) {
        ErrorResponse response = new ErrorResponse(
                new ErrorResponse.ErrorDetail(
                        ex.getCode(),
                        ex.getMessage()
                )
        );

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(response);
    }
}
