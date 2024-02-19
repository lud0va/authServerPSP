package com.example.authseverproyectopsp.spring.rest.security;

import com.example.authseverproyectopsp.common.Configuration;
import com.example.authseverproyectopsp.common.Constantes;
import com.example.authseverproyectopsp.domain.model.Credentials;
import com.example.authseverproyectopsp.domain.model.Errors;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.vavr.control.Either;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class TokensGenerator {

    private final Configuration co;

    public TokensGenerator(Configuration co) {
        this.co = co;
    }


    public Either<Errors, String> generateAccessToken(Credentials credentials) {
        try {
            // Cargar el keystore
            KeyStore keyStore = KeyStore.getInstance(Constantes.PKCS_12);
            keyStore.load(new FileInputStream(co.getNombreKeystore()), co.getClave().toCharArray());

            // Obtener la clave privada del servidor
            KeyStore.PasswordProtection keyPassword = new KeyStore.PasswordProtection(co.getClave().toCharArray());
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(co.getServerName(), keyPassword);
            PrivateKey privateKey = privateKeyEntry.getPrivateKey();


            // Construir el token JWT

            String accesToken = Jwts.builder()
                    .setSubject(credentials.getUserName())
                    .claim(Constantes.ROLE, credentials.getRol())
                    .setExpiration(Date
                            .from(LocalDateTime.now().plusSeconds(180)
                                    .atZone(ZoneId.systemDefault()).toInstant()))
                    .signWith(privateKey)

                    .compact();
            return Either.right(accesToken);
        } catch (CertificateException | KeyStoreException | IOException | NoSuchAlgorithmException |
                 UnrecoverableEntryException e) {
            Logger.getLogger(TokensGenerator.class.getName()).log(Level.SEVERE, null, e);
            return Either.left(new Errors(Constantes.ERROR_FIRMANDO_EL_ACCESS_TOKEN));

        }
    }

    public Either<Errors, String> getNewAccesTokenFromRefreshToken(String header) {
        try {
            // Cargar el keystore
            KeyStore keyStore = KeyStore.getInstance(Constantes.PKCS_12);
            keyStore.load(new FileInputStream(co.getNombreKeystore()), co.getClave().toCharArray());

            // Obtener la clave privada del servidor
            KeyStore.PasswordProtection keyPassword = new KeyStore.PasswordProtection(co.getClave().toCharArray());
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(co.getServerName(), keyPassword);
            PrivateKey privateKey = privateKeyEntry.getPrivateKey();

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(privateKey)
                    .build()
                    .parseClaimsJws(header)
                    .getBody();
            String accesToken = Jwts.builder()
                    .setSubject(claims.get(Constantes.USERNAME).toString())
                    .claim(Constantes.ROLE, claims.get(Constantes.ROLE))
                    .setExpiration(Date
                            .from(LocalDateTime.now().plusSeconds(60)
                                    .atZone(ZoneId.systemDefault()).toInstant()))
                    .signWith(privateKey)

                    .compact();
            return Either.right(accesToken);
        } catch (CertificateException | KeyStoreException | IOException | NoSuchAlgorithmException |
                 UnrecoverableEntryException e) {
            Logger.getLogger(TokensGenerator.class.getName()).log(Level.SEVERE, null, e);
            return Either.left(new Errors(Constantes.ERROR_FIRMANDO_EL_ACCESS_TOKEN));

        }
    }


    public Either<Errors, String> generateRefreshToken(Credentials credentials) {
        try {
            // Cargar el keystore
            KeyStore keyStore = KeyStore.getInstance(Constantes.PKCS_12);
            keyStore.load(new FileInputStream(co.getNombreKeystore()), co.getClave().toCharArray());

            // Obtener la clave privada del servidor
            KeyStore.PasswordProtection keyPassword = new KeyStore.PasswordProtection(co.getClave().toCharArray());
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(co.getServerName(), keyPassword);
            PrivateKey privateKey = privateKeyEntry.getPrivateKey();


            // Construir el token JWT
            String accesToken = Jwts.builder()
                    .setSubject(credentials.getUserName())
                    .claim(Constantes.ROLE, credentials.getRol())
                    .claim(Constantes.USERNAME, credentials.getUserName())
                    .setExpiration(Date.from(LocalDateTime.now().plusMinutes(60)
                            .atZone(ZoneId.systemDefault()).toInstant()))

                    .signWith(privateKey)

                    .compact();
            return Either.right(accesToken);
        } catch (CertificateException | KeyStoreException | IOException | NoSuchAlgorithmException |
                 UnrecoverableEntryException e) {
            Logger.getLogger(TokensGenerator.class.getName()).log(Level.SEVERE, null, e);
            return Either.left(new Errors(Constantes.ERROR_FIRMANDO_EL_ACCESS_TOKEN));

        }
    }
}
