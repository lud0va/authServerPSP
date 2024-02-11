package com.example.authseverproyectopsp.services;

import com.example.authseverproyectopsp.common.Configuration;
import com.example.authseverproyectopsp.data.dao.CredentialsDao;
import com.example.authseverproyectopsp.data.model.Credentials;
import com.example.authseverproyectopsp.data.model.Errors;
import com.example.authseverproyectopsp.security.TokensGenerator;
import io.vavr.control.Either;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CredentialsService {
    private final CredentialsDao dao;
    private final Configuration co;
    private final TokensGenerator tokensGenerator;


    public CredentialsService(CredentialsDao dao, Configuration co, TokensGenerator tokensGenerator) {
        this.dao = dao;
        this.co = co;
        this.tokensGenerator = tokensGenerator;
    }

    public Either<Errors, Integer> register(String name, String passw) {
        String str = co.createPasswordEncoder().encode(passw);
        dao.save(new Credentials(name, str));
        return Either.right(0);
    }

    public Either<Errors,List<String>>login(String name, String passw){
        Credentials credentials = dao.findByUserName(name).orElseThrow();
        List<String> tokens=new ArrayList<>();
        if (co.createPasswordEncoder().matches(passw, credentials.getPassword())) {

            tokens.add(tokensGenerator.generateAccessToken(new Credentials(name,credentials.getPassword())).get());
            tokens.add(tokensGenerator.generateRefreshToken(new Credentials(name,credentials.getPassword())).get());
        }

        return Either.right(tokens);
    }

    public Either<Errors,String> getAccessToken(String header){
        return tokensGenerator.getNewAccesTokenFromRefreshToken(header);

    }
}
