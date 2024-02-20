package com.example.authseverproyectopsp.spring.rest.errors.exceptions;

public class UserExistException extends RuntimeException{
    public UserExistException() {
        super("Ese user ya existe");
    }
}
