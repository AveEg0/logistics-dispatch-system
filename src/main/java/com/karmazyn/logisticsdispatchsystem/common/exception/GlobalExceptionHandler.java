package com.karmazyn.logisticsdispatchsystem.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the application.
 * Intercepts various exceptions and returns appropriate HTTP response codes and messages.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles validation errors for request bodies.
     * Returns a map of field names and their corresponding error messages.
     *
     * @param ex the validation exception
     * @return a map containing validation errors
     */
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

    /**
     * Handles exceptions related to resources that are not found (404 Not Found).
     *
     * @param ex the runtime exception containing the error message
     * @return the error message string
     */
    @ExceptionHandler({
            UserNotFoundException.class,
            DriverNotFoundException.class,
            OrderNotFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(RuntimeException ex) {
        return ex.getMessage();
    }

    /**
     * Handles business logic conflicts or invalid states (409 Conflict).
     *
     * @param ex the runtime exception containing the error message
     * @return the error message string
     */
    @ExceptionHandler({
            DriverNotAvailableException.class,
            EmailAlreadyExistsException.class,
            IllegalStateException.class
    })
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleConflict(RuntimeException ex) {
        return ex.getMessage();
    }

    /**
     * Handles bad request exceptions (400 Bad Request).
     *
     * @param ex the runtime exception containing the error message
     * @return the error message string
     */
    @ExceptionHandler({
            InvalidPasswordException.class,
            IllegalArgumentException.class,
            InvalidPrincipalException.class,
            InvalidRefreshTokenException.class,
            RefreshTokenRevokedException.class,
            RefreshTokenExpiredException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBadRequest(RuntimeException ex) {
        return ex.getMessage();
    }

    /**
     * Handles unauthorized access (401 Unauthorized).
     *
     * @param ex the user not authenticated exception
     * @return the error message string
     */
    @ExceptionHandler(UserNotAuthenticatedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handleUnauthorized(UserNotAuthenticatedException ex) {
        return ex.getMessage();
    }

    /**
     * Handles exceptions related to unauthorized role actions (403 Forbidden).
     *
     * @param ex the invalid user role exception
     * @return the error message string
     */
    @ExceptionHandler(InvalidUserRoleException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleForbidden(InvalidUserRoleException ex) {
        return ex.getMessage();
    }

    /**
     * Fallback handler for any unexpected exceptions (500 Internal Server Error).
     *
     * @param ex the exception
     * @return a generic error message string
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGeneric(Exception ex) {
        return "An unexpected error occurred: " + ex.getMessage();
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handle(AccessDeniedException ex) {
        return ResponseEntity.status(403).body("Forbidden");
    }
}
