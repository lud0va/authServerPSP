package com.example.authseverproyectopsp.spring.rest.errors.exceptions;

import com.example.authseverproyectopsp.common.Constantes;

public class UserExistException extends RuntimeException{

    public UserExistException() {
        super(Constantes.ESE_USER_YA_EXISTE);
    }
}
