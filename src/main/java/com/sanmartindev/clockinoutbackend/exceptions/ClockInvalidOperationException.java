package com.sanmartindev.clockinoutbackend.exceptions;

public class ClockInvalidOperationException extends RuntimeException{
    public ClockInvalidOperationException(String message) {
        super(message);
    }
}
