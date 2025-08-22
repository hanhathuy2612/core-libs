package com.hnh.enterprise.core.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.Serial;
import java.text.MessageFormat;

/**
 * Custom runtime exception for the application that encapsulates error information.
 * This exception is used to handle application-specific errors and provides
 */
@Getter
public class AppRuntimeException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;
    
    private final String message;
    private final HttpStatus status;
    private final String code;
    
    /**
     * Constructor for AppRuntimeException.
     * @param error the error information encapsulated in an IAppErrorInfo instance
     */
    public AppRuntimeException(IAppErrorInfo error) {
        this.message = error.getMessage();
        this.status = error.getDefaultStatus();
        this.code = error.getCode();
    }
    
    /**
     * Constructor for AppRuntimeException with formatted message.
     * @param error the error information encapsulated in an IAppErrorInfo instance
     * @param messageProps the properties to format the error message
     */
    public AppRuntimeException(IAppErrorInfo error, Object... messageProps) {
        this.message = MessageFormat.format(error.getMessage(), messageProps);
        this.status = error.getDefaultStatus();
        this.code = error.getCode();
    }
}

