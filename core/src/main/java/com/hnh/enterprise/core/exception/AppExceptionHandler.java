package com.hnh.enterprise.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.hnh.enterprise.core.rest.response.BaseResponse.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * Handles exceptions globally for the application.
 */
@Slf4j
public class AppExceptionHandler {

    /**
     * Handles MethodArgumentNotValidException, which is thrown when a method
     * argument fails validation.
     * 
     * @param ex the exception that was thrown
     * @return a ResponseEntity containing an ErrorResponse with a BAD_REQUEST
     *         status
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse<?>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        log.error("MethodArgumentNotValidException", ex);
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .message(ex.getMessage())
                        .build(),
                HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles AppRuntimeException, which is a custom runtime exception for the
     * application.
     * 
     * @param ex the exception that was thrown
     * @return a ResponseEntity containing an ErrorResponse with the status and
     *         message from the exception
     */
    @ExceptionHandler(AppRuntimeException.class)
    public ResponseEntity<ErrorResponse<?>> handle(AppRuntimeException ex) {
        log.error("AppRuntimeException", ex);
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .code(ex.getStatus().value())
                        .message(ex.getMessage())
                        .status(ex.getStatus())
                        .build(),
                ex.getStatus());
    }

    /**
     * Handles AccessDeniedException, which is thrown when access to a resource is
     * denied.
     * 
     * @param ex the exception that was thrown
     * @return a ResponseEntity containing an ErrorResponse with a FORBIDDEN status
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse<?>> handle(AccessDeniedException ex) {
        log.error("AccessDeniedException", ex);
        return buildErrorResponse(ex, HttpStatus.FORBIDDEN);
    }

    /**
     * Handles IllegalArgumentException, which is thrown when an illegal or
     * inappropriate argument is passed.
     * 
     * @param ex the exception that was thrown
     * @return a ResponseEntity containing an ErrorResponse with a BAD_REQUEST
     *         status
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse<?>> handle(IllegalArgumentException ex) {
        log.error("IllegalArgumentException", ex);
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles all other exceptions that are not specifically handled by other
     * methods.
     * 
     * @param ex the exception that was thrown
     * @return a ResponseEntity containing an ErrorResponse with an
     *         INTERNAL_SERVER_ERROR status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse<?>> handle(Exception ex) {
        log.error("InternalServerError", ex);
        return buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Builds a standardized error response for exceptions.
     * 
     * @param exception the exception that was thrown
     * @param status    the HTTP status to return
     * @return a ResponseEntity containing an ErrorResponse with the specified
     *         status and exception message
     */
    private ResponseEntity<ErrorResponse<?>> buildErrorResponse(Exception exception, HttpStatus status) {
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .message(exception.getMessage())
                        .code(status.value())
                        .build(),
                status);
    }

}
