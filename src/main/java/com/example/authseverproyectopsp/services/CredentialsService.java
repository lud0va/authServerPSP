package com.example.authseverproyectopsp.services;

import com.example.authseverproyectopsp.common.Configuration;
import com.example.authseverproyectopsp.data.dao.CredentialsDao;
import com.example.authseverproyectopsp.data.model.Credentials;
import com.example.authseverproyectopsp.data.model.Errors;
import com.example.authseverproyectopsp.security.TokensGenerator;
import io.vavr.control.Either;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CredentialsService {
    private final CredentialsDao dao;
    private final Configuration co;
    private final TokensGenerator tokensGenerator;
    private final AuthenticationManager authenticationManager;

    public CredentialsService(CredentialsDao dao, Configuration co, TokensGenerator tokensGenerator, AuthenticationManager authenticationManager) {
        this.dao = dao;
        this.co = co;
        this.tokensGenerator = tokensGenerator;
        this.authenticationManager = authenticationManager;
    }
    public Either<Errors, Integer> register(String name, String passw) {
        String str = co.createPasswordEncoder().encode(passw);
        dao.save(new Credentials(name, str));
        return Either.right(0);
    }


    public Either<Errors,List<String>>login(String name, String passw) {
        Authentication auth =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(name, passw));
        List<String> tokens = new ArrayList<>();
        if (auth.isAuthenticated()){
            Credentials credentials=new Credentials(name,passw);
            tokens.add(tokensGenerator.generateAccessToken(credentials).get());
            tokens.add(tokensGenerator.generateRefreshToken(credentials).get());
            return Either.right(tokens);

        }else {
            return Either.left(new Errors("Usuario invalido"));
        }





    }
    public Either<Errors,String> getAccessToken(String header){
        return tokensGenerator.getNewAccesTokenFromRefreshToken(header);

    }
}
