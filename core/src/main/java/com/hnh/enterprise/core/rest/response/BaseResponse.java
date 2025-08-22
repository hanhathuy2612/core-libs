package com.hnh.enterprise.core.rest.response;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseResponse<T> {
    protected HttpStatus status;
    protected T data;
    protected String message;
    protected boolean success;

    /**
     * Create success response with data
     */
    public static <T> SuccessResponse<T> of(T data) {
        return SuccessResponse.<T>builder()
                .data(data)
                .success(true)
                .status(HttpStatus.OK)
                .build();
    }

    /**
     * Create error response with message
     */
    public static <T> ErrorResponse<T> error(String message) {
        return ErrorResponse.<T>builder()
                .message(message)
                .success(false)
                .status(HttpStatus.BAD_REQUEST)
                .build();
    }

    /**
     * Concrete success response implementation
     */
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    public static class SuccessResponse<T> extends BaseResponse<T> {
    }

    /**
     * Concrete error response implementation
     */
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    public static class ErrorResponse<T> extends BaseResponse<T> {
        private int code;
    }
}
