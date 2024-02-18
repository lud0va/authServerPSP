package com.example.authseverproyectopsp.domain.services;

import com.example.authseverproyectopsp.common.Configuration;
import com.example.authseverproyectopsp.common.Constantes;
import com.example.authseverproyectopsp.data.dao.CredentialsDao;
import com.example.authseverproyectopsp.domain.model.Credentials;
import com.example.authseverproyectopsp.domain.model.Errors;
import com.example.authseverproyectopsp.spring.auth.AuthenticationResponse;
import com.example.authseverproyectopsp.spring.rest.errors.CredentialInvalid;
import com.example.authseverproyectopsp.spring.rest.security.TokensGenerator;
import io.vavr.control.Either;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
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

    public boolean register(String name, String passw) {
        String str = co.createPasswordEncoder().encode(passw);
        dao.save(new Credentials(name, str));
        return true;
    }


    public AuthenticationResponse login(String name, String passw) {
        try {
            Authentication auth =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(name, passw));
            List<String> tokens = new ArrayList<>();
            if (auth.isAuthenticated()) {
                Credentials credentials = dao.findByUserName(name).get();
                tokens.add(tokensGenerator.generateAccessToken(credentials).get());
                tokens.add(tokensGenerator.generateRefreshToken(credentials).get());
                return AuthenticationResponse.builder()
                        .accessToken(tokens.get(0))
                        .refreshToken(tokens.get(1))
                        .build();

            } else {
                throw new CredentialInvalid("");
            }
        } catch (BadCredentialsException e) {
            throw new CredentialInvalid(e.getMessage());
        }

    }
        public Either<Errors, String> getAccessToken(String header) {
        return tokensGenerator.getNewAccesTokenFromRefreshToken(header);

    }
}
