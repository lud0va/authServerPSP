package com.example.authseverproyectopsp.spring.rest;


import com.example.authseverproyectopsp.data.model.Errors;
import com.example.authseverproyectopsp.services.CredentialsService;
import io.vavr.control.Either;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class RestLogin {
    private final CredentialsService serv;


    public RestLogin(CredentialsService serv) {
        this.serv = serv;
    }

    @GetMapping("/login")
    public List<String> login(@RequestParam(name = "username",required = true) String username, @RequestParam(name = "password",required = false) String password){
        return  serv.login(username,password).get();

    }
    @GetMapping("/getAccessToken")
    public String getAccessToken(@RequestParam(name="refreshtoken")String refresh){
        Either<Errors,String>  result =serv.getAccessToken(refresh);
        if (result.isRight()){
            return  result.get();
        }else {
            return result.getLeft().getMsg();
        }

    }


}