package com.example.authseverproyectopsp.spring.rest;


import com.example.authseverproyectopsp.common.Constantes;
import com.example.authseverproyectopsp.domain.services.CredentialsService;
import com.example.authseverproyectopsp.spring.auth.AuthenticationResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;




@RestController
public class RestLogin {

    private final CredentialsService serv;


    public RestLogin(CredentialsService serv) {
        this.serv = serv;

    }

    @GetMapping(Constantes.LOGIN)
    public AuthenticationResponse login(@RequestParam(name = Constantes.USERNAMEP, required = true) String username, @RequestParam(name = Constantes.PASSWORD, required = false) String password) {


        return serv.login(username, password);

    }

    @GetMapping(Constantes.REGISTER)
    public Boolean register(@RequestParam(name = Constantes.USERNAMEP) String username, @RequestParam(name = Constantes.PASSWORD) String password) {
        return serv.register(username, password);
    }

    @GetMapping(Constantes.GETACCESSTOKEN)
    public String getAccessToken(@RequestParam(name = Constantes.REFRESHTOKEN) String refresh) {
        return serv.getAccessToken(refresh);


    }


}