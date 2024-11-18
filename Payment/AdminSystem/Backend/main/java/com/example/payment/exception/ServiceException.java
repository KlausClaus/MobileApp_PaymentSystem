package com.example.payment.exception;

import lombok.Getter;

/**
 * Custom exception class for handling business logic errors.
 * Extends {@link RuntimeException} to allow unchecked exceptions.
 */
@Getter
public class ServiceException extends RuntimeException {

    /**
     * Error code associated with the exception.
     */
    private String code;

    /**
     * Constructs a new {@code ServiceException} with the specified error code and message.
     *
     * @param code The error code representing the type of exception.
     * @param msg  The detail message associated with the exception.
     */
    public ServiceException(String code, String msg) {
        super(msg);
        this.code = code;
    }
}
