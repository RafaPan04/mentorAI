package com.fiap.gs.demo.exceptions;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Builder;

@ControllerAdvice
public class ExceptionController {
     @Builder
    public static record ExceptionResponse (
        Integer statusCode,
        String message,
        Timestamp timestamp,
        String path
    ) {}

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        var errors = exception.getFieldErrors();
        return ResponseEntity.badRequest()
                .body(errors.stream().map(InnerTratadorDeErros::new).toList());
    }

    private record InnerTratadorDeErros(String campo, String messagem) {
        public InnerTratadorDeErros(FieldError field) {
            this(field.getField(), field.getDefaultMessage());
        }
    }


    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBadRequestException(BadRequestException ex, HttpServletRequest request) {
        ExceptionResponse exceptionResponse = buildExceptionResponse(ex, request, HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(NotFoundException ex, HttpServletRequest request) {
        ExceptionResponse exceptionResponse = buildExceptionResponse(ex, request, HttpStatus.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionResponse);
    }


    private ExceptionResponse buildExceptionResponse(Exception ex, HttpServletRequest request, HttpStatus status) {
       
        return ExceptionResponse.builder()
                .statusCode(status.value())
                .message(ex.getMessage())
                .timestamp(Timestamp.valueOf(LocalDateTime.now()))
                .path(request.getRequestURI())
                .build();
    }
}
