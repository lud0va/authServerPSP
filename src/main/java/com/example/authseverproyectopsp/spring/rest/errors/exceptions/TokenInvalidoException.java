package com.example.authseverproyectopsp.spring.rest.errors.exceptions;

public class TokenInvalidoException extends RuntimeException{
    public TokenInvalidoException(String message) {
        super("Token invalido  "+message);
    }
}
