package com.summarise.webpage.exceptions;

import com.summarise.webpage.model.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GeminiException.class)
    public ResponseEntity<ErrorResponse> handleGeminiException(
            GeminiException ex,
            HttpServletRequest request) {

        HttpStatus status;

        switch (ex.getType()) {
            case INVALID_PROMPT -> status = HttpStatus.BAD_REQUEST;
            case MODEL_OUTPUT_EMPTY -> status = HttpStatus.UNPROCESSABLE_ENTITY;
            case THIRD_PARTY_UNAVAILABLE -> status = HttpStatus.BAD_GATEWAY;
            case RATE_LIMITED -> status = HttpStatus.TOO_MANY_REQUESTS;
            default -> status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        ErrorResponse response = new ErrorResponse(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getRequestURI(),
                status.value()
        );

        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex,
            HttpServletRequest request) {

        ErrorResponse response = new ErrorResponse(
                LocalDateTime.now(),
                "Unexpected server error",
                request.getRequestURI(),
                500
        );

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

