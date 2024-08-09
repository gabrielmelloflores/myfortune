package com.gabrielflores.myfortune.exception;

/**
 *
 * @author Douglas Pasqualin <douglas.pasqualin@gmail.com>
 */
public class PagSeguroException extends RuntimeException {

    public PagSeguroException(String message) {
        super("PagSeguroException: " + message);
    }

    public PagSeguroException(String message, Throwable cause) {
        super("PagSeguroException: " + message, cause);
    }
}
