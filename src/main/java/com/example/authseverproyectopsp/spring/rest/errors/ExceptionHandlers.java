package com.example.authseverproyectopsp.spring.rest.errors;

import com.example.authseverproyectopsp.domain.model.Errors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlers  {
    @ExceptionHandler(CredentialInvalid.class)
    public ResponseEntity<Errors> handleException(CredentialInvalid e) {
        Errors apiError = new Errors(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }
}
