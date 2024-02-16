package com.inclockgnito.exceptions;

import org.springframework.security.authentication.BadCredentialsException;

public class UsernameAlreadyInUseException extends BadCredentialsException {
    public UsernameAlreadyInUseException(String msg) {
        super(msg);
    }
}
