package com.evaluacion2023.config.exceptions;

import org.antlr.v4.runtime.Token;

public class TokenRevokedException extends RuntimeException{
    public TokenRevokedException(String message) {
        super(message);
    }
}
