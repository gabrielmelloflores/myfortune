package com.gabrielflores.myfortune.exception;

/**
 *
 * @Author: Gabriel Flores - gabrielmelloflores@gmail.com  
 */
public class RegraNegocioException extends RuntimeException {

    public RegraNegocioException() {
    }

    public RegraNegocioException(String msg) {
        super(msg);
    }

    public RegraNegocioException(String message, Throwable cause) {
        super(message, cause);
    }

}
