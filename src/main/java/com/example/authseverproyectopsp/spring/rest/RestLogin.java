package com.example.authseverproyectopsp.spring.rest;


import com.example.authseverproyectopsp.data.model.Errors;
import com.example.authseverproyectopsp.services.CredentialsService;
import io.vavr.control.Either;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController

public class RestLogin {
    private final CredentialsService serv;


    public RestLogin(CredentialsService serv) {
        this.serv = serv;

    }

    @GetMapping("/login")
    public List<String> login(@RequestParam(name = "username",required = true) String username, @RequestParam(name = "password",required = false) String password){
       List<String> r=serv.login(username,password).get();
       List<String>x=new ArrayList<>();
       x.add("access");
       x.add(r.get(0));
       x.add("refresh");
       x.add(r.get(1));

        return x ;

    }

    @GetMapping("/register")
    public Boolean register(@RequestParam(name = "username")String username,@RequestParam(name = "password")String password){
        Either<Errors,Integer>  result =serv.register(username,password);
        return result.isRight();
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