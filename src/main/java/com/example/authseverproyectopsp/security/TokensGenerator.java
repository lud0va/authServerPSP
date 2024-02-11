package com.example.authseverproyectopsp.security;

import com.example.authseverproyectopsp.common.Configuration;
import com.example.authseverproyectopsp.data.model.Credentials;
import com.example.authseverproyectopsp.data.model.Errors;
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


    public Either<Errors,String> generateAccessToken(Credentials credentials){
        try {
            // Cargar el keystore
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(new FileInputStream(co.getNombreKeystore()), co.getClave().toCharArray());

            // Obtener la clave privada del servidor
            KeyStore.PasswordProtection keyPassword = new KeyStore.PasswordProtection(co.getClave().toCharArray());
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(co.getServerName(), keyPassword);
            PrivateKey privateKey = privateKeyEntry.getPrivateKey();


            // Construir el token JWT

            String accesToken = Jwts.builder()
                    .setSubject(credentials.getUserName())
                    .claim("role", credentials.getRol())
                    .setExpiration(Date
                            .from(LocalDateTime.now().plusSeconds(180)
                                    .atZone(ZoneId.systemDefault()).toInstant()))
                    .signWith( privateKey)

                    .compact();
            return Either.right(accesToken);
        } catch (CertificateException | KeyStoreException | IOException | NoSuchAlgorithmException | UnrecoverableEntryException  e) {
            Logger.getLogger(TokensGenerator.class.getName()).log(Level.SEVERE, null, e);
            return Either.left(new Errors("error firmando el access token"));

        }
    }
    public Either<Errors,String> getNewAccesTokenFromRefreshToken(String header){
        try {
            // Cargar el keystore
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(new FileInputStream(co.getNombreKeystore()), co.getClave().toCharArray());

            // Obtener la clave privada del servidor
            KeyStore.PasswordProtection keyPassword = new KeyStore.PasswordProtection(co.getClave().toCharArray());
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(co.getServerName(), keyPassword);
            PrivateKey privateKey = privateKeyEntry.getPrivateKey();

            // Construir el token JWT
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(privateKey)
                    .build()
                    .parseClaimsJws(header)
                    .getBody();
            String accesToken = Jwts.builder()
                    .setSubject(claims.get("username").toString())
                    .claim("role", claims.get("role"))
                    .setExpiration(Date
                            .from(LocalDateTime.now().plusSeconds(180)
                                    .atZone(ZoneId.systemDefault()).toInstant()))
                    .signWith( privateKey)

                    .compact();
            return Either.right(accesToken);
        } catch (CertificateException | KeyStoreException | IOException | NoSuchAlgorithmException | UnrecoverableEntryException  e) {
            Logger.getLogger(TokensGenerator.class.getName()).log(Level.SEVERE, null, e);
            return Either.left(new Errors("error firmando el access token"));

        }
    }


    public Either<Errors,String> generateRefreshToken(Credentials credentials){
        try {
            // Cargar el keystore
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(new FileInputStream(co.getNombreKeystore()), co.getClave().toCharArray());

            // Obtener la clave privada del servidor
            KeyStore.PasswordProtection keyPassword = new KeyStore.PasswordProtection(co.getClave().toCharArray());
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(co.getServerName(), keyPassword);
            PrivateKey privateKey = privateKeyEntry.getPrivateKey();


            // Construir el token JWT
            String accesToken = Jwts.builder()
                    .setSubject(credentials.getUserName())
                    .claim("role", credentials.getRol())
                    .claim("username", credentials.getUserName())
                    .setExpiration(Date.from(LocalDateTime.now().plusMinutes(10)
                            .atZone(ZoneId.systemDefault()).toInstant()))

                    .signWith(privateKey)

                    .compact();
            return Either.right(accesToken);
        } catch (CertificateException | KeyStoreException | IOException | NoSuchAlgorithmException | UnrecoverableEntryException  e) {
            Logger.getLogger(TokensGenerator.class.getName()).log(Level.SEVERE, null, e);
            return Either.left(new Errors("error firmando el access token"));

        }
    }
}
