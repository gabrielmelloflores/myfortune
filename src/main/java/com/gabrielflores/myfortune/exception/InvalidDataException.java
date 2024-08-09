package com.gabrielflores.myfortune.exception;

/**
 *
 * @author Douglas Pasqualin <douglas.pasqualin@gmail.com>
 */
public class InvalidDataException extends RuntimeException {

    public InvalidDataException(String message) {
        super(message);
    }

    public InvalidDataException(String message, Throwable cause) {
        super(message, cause);
    }

}
