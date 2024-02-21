package com.example.authseverproyectopsp.spring.rest.errors.exceptions;

import com.example.authseverproyectopsp.common.Constantes;

public class TokenInvalidoException extends RuntimeException{

    public TokenInvalidoException(String message) {
        super(Constantes.TOKEN_INVALIDO +message);
    }
}
