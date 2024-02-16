package com.example.authseverproyectopsp.spring.rest.errors;

public class CredentialInvalid extends RuntimeException {
    public CredentialInvalid(String message) {
        super(message);
    }
}
