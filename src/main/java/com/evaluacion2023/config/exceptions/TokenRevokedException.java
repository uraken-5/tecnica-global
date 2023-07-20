package com.evaluacion2023.config.exceptions;

public class TokenRevokedException extends RuntimeException{
    public TokenRevokedException(String message) {
        super(message);
    }
}
