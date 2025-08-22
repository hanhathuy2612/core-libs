package com.hnh.enterprise.core.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * AppErrorInfo
 */
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum AppErrorInfo implements IAppErrorInfo {
    /**
     * Error when ID parameter is null
     */
    ID_CANNOT_BE_NULL("ID_CANNOT_BE_NULL", "Id can't be null", HttpStatus.BAD_REQUEST),
    
    /**
     * Error when requested entity is not found
     */
    ENTITY_NOT_FOUND("ENTITY_NOT_FOUND", "Entity not found", HttpStatus.BAD_REQUEST),
    
    /**
     * Error for malformed or invalid requests
     */
    BAD_REQUEST("BAD_REQUEST", "Bad Request", HttpStatus.BAD_REQUEST),
    
    /**
     * Error when user lacks required permissions
     */
    PERMISSION_DENIED("PERMISSION_DENIED", "Permission Denied", HttpStatus.FORBIDDEN),
    
    /**
     * Error when user is not authenticated
     */
    UNAUTHORIZED("UNAUTHORIZED", "Unauthorized", HttpStatus.UNAUTHORIZED),
    
    /**
     * Error for unexpected server-side failures
     */
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR),
    
    /**
     * Error when service is temporarily unavailable
     */
    SERVICE_UNAVAILABLE("SERVICE_UNAVAILABLE", "Service Unavailable", HttpStatus.SERVICE_UNAVAILABLE),
    ;
    
    private final String code;
    private final String message;
    private final HttpStatus defaultStatus;
    
    AppErrorInfo(String code, String message, HttpStatus defaultStatus) {
        this.code = code;
        this.message = message;
        this.defaultStatus = defaultStatus;
    }
}
