package com.karmazyn.logisticsdispatchsystem.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 400 - Validation Errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    // 404 - Not Found
    @ExceptionHandler({
            UserNotFoundException.class,
            DriverNotFoundException.class,
            OrderNotFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(RuntimeException ex) {
        return ex.getMessage();
    }

    // 409 - Business Conflict
    @ExceptionHandler({
            DriverNotAvailableException.class,
            EmailAlreadyExistsException.class,
            IllegalStateException.class
    })
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleConflict(RuntimeException ex) {
        return ex.getMessage();
    }

    // 403 - Forbidden
    @ExceptionHandler(InvalidUserRoleException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleForbidden(InvalidUserRoleException ex) {
        return ex.getMessage();
    }

    // 500 - Generic Error
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGeneric(Exception ex) {
        return "An unexpected error occurred: " + ex.getMessage();
    }
}
