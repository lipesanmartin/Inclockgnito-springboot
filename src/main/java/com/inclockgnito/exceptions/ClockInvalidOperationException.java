package com.inclockgnito.exceptions;

public class ClockInvalidOperationException extends RuntimeException{
    public ClockInvalidOperationException(String message) {
        super(message);
    }
}
