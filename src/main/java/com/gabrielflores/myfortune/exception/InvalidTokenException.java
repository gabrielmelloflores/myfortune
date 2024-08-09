package com.gabrielflores.myfortune.exception;

import org.springframework.security.core.AuthenticationException;

/**
 *
 * @Author: Gabriel Flores - gabrielmelloflores@gmail.com  
 */
public class InvalidTokenException extends AuthenticationException {

    private static final long serialVersionUID = 6251929475506851452L;

    public InvalidTokenException(String msg) {
        super(msg);
    }

    public InvalidTokenException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
