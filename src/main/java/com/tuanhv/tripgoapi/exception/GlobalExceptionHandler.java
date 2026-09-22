package com.tuanhv.tripgoapi.exception;

import com.tuanhv.tripgoapi.dto.response.ErrorResponse;
import com.tuanhv.tripgoapi.dto.response.ValidationErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

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

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        Map<String, String> fields = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(
                        Collectors.toMap(
                                FieldError::getField,
                                error -> Objects.requireNonNullElse(
                                        error.getDefaultMessage(),
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

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(
            DataIntegrityViolationException ex
    ) {
        log.warn("Database constraint violation", ex);

        ErrorResponse response = new ErrorResponse(
                new ErrorResponse.ErrorDetail(
                        "DATA_CONFLICT",
                        "Dữ liệu xung đột với trạng thái hiện tại"
                )
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflict(ConflictException ex) {
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

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(UnauthorizedException ex) {
        ErrorResponse response = new ErrorResponse(
                new ErrorResponse.ErrorDetail(
                        ex.getCode(),
                        ex.getMessage()
                )
        );

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }
}
