package com.evaluacion2023.config.exceptions;

public class InvalidAuthorizationHeaderException extends RuntimeException {

    public InvalidAuthorizationHeaderException(String message) {
        super(message);
    }

}