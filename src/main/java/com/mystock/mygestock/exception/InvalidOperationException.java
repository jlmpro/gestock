package com.mystock.mygestock.exception;

import lombok.Getter;

@Getter
public class InvalidOperationException extends RuntimeException {
    private ErrorCodes errorCodes ;

    public InvalidOperationException(String message){
        super(message);
    }
    public InvalidOperationException(String message, Throwable cause){
        super(message,cause);
    }
    public InvalidOperationException(String message, Throwable cause, ErrorCodes errorCodes){
        super(message, cause);
    }
    public InvalidOperationException(String message, ErrorCodes errorCodes){
        super(message);
        this.errorCodes =errorCodes;
    }

}
