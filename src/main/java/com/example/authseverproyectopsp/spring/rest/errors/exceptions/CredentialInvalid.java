package com.example.authseverproyectopsp.spring.rest.errors.exceptions;

public class CredentialInvalid extends RuntimeException {
    public CredentialInvalid(String message) {
        super(message);
    }
}
