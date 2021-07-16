package com.example.chatserver.exceptions;

import org.springframework.http.HttpStatus;

public class MyNotFoundException extends RuntimeException {
    private final HttpStatus httpStatus = HttpStatus.NOT_FOUND;

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public MyNotFoundException(String message) {
        super(message);
    }
}
