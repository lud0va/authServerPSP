package com.example.authseverproyectopsp.spring.rest;


import com.example.authseverproyectopsp.common.Constantes;
import com.example.authseverproyectopsp.data.model.Errors;
import com.example.authseverproyectopsp.services.CredentialsService;
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

    public List<String> login(@RequestParam(name =  Constantes.USERNAMEP, required = true) String username, @RequestParam(name = Constantes.PASSWORD, required = false) String password) {
        List<String> r = serv.login(username, password).get();
        List<String> x = new ArrayList<>();
        x.add(Constantes.ACCESS);
        x.add(r.get(0));
        x.add(Constantes.REFRESH);
        x.add(r.get(1));

        return x;

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