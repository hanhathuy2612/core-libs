package com.hnh.enterprise.core.exception;

import org.springframework.http.HttpStatus;

public interface IAppErrorInfo {

    String getCode();

    String getMessage();
    
    HttpStatus getDefaultStatus();
}
