package com.example.authseverproyectopsp.spring.rest;


import com.example.authseverproyectopsp.common.Constantes;
import com.example.authseverproyectopsp.domain.model.Errors;
import com.example.authseverproyectopsp.domain.services.CredentialsService;
import com.example.authseverproyectopsp.spring.auth.AuthenticationResponse;
import io.vavr.control.Either;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
public class RestLogin {

    private final CredentialsService serv;


    public RestLogin(CredentialsService serv) {
        this.serv = serv;

    }

    @GetMapping(Constantes.LOGIN)
    public AuthenticationResponse login(@RequestParam(name =  Constantes.USERNAMEP, required = true) String username, @RequestParam(name = Constantes.PASSWORD, required = false) String password) {



        return serv.login(username, password);

    }

    @GetMapping(Constantes.REGISTER)
    public Boolean register(@RequestParam(name = Constantes.USERNAMEP) String username, @RequestParam(name = Constantes.PASSWORD) String password) {
        Boolean result = serv.register(username, password);
        return result.booleanValue();
    }

    @GetMapping(Constantes.GETACCESSTOKEN)
    public String getAccessToken(@RequestParam(name = Constantes.REFRESHTOKEN) String refresh) {
        Either<Errors, String> result = serv.getAccessToken(refresh);
        if (result.isRight()) {
            return result.get();
        } else {
            return result.getLeft().getMsg();
        }

    }


}