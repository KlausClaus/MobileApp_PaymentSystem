package com.example.payment.exception;

import com.example.payment.common.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Global exception handler to manage exceptions across the application.
 * Provides centralized handling of exceptions to return consistent error responses.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles exceptions of type {@link ServiceException}.
     * This method is invoked when a {@code ServiceException} is thrown.
     *
     * @param se The service exception instance containing the error code and message.
     * @return A {@code Result} object containing the error code and message.
     */
    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public Result handle(ServiceException se) {
        return Result.error(se.getCode(), se.getMessage());
    }
}
