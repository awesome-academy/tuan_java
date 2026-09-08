package com.tuanhv.tripgoapi.exception;

import lombok.Getter;

@Getter
public class BookingConflictException extends RuntimeException {

    private final String code;

    public BookingConflictException(String code, String message) {
        super(message);
        this.code = code;
    }
}
