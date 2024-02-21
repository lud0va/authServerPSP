package com.example.authseverproyectopsp.spring.rest.security;

import com.example.authseverproyectopsp.common.Constantes;
import com.example.authseverproyectopsp.data.dao.CredentialsDao;
import com.example.authseverproyectopsp.domain.model.Credentials;
import com.example.authseverproyectopsp.spring.rest.errors.exceptions.TokenInvalidoException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${application.security.jwt.keystoreName}")
    private String keystorename;
    @Value("${application.security.jwt.clave}")
    private String claveKeystore;
    @Value("${application.security.jwt.serverName}")
    private String serverName;
    @Value("${application.security.jwt.access-expiration}")
    private long refreshExpiration;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private  long access;


    private final CredentialsDao dao;

    public TokensGenerator( CredentialsDao dao) {

        this.dao = dao;
    }


    public String generateAccessToken(Credentials credentials) {
        try {
            // Cargar el keystore
            KeyStore keyStore = KeyStore.getInstance(Constantes.PKCS_12);
            keyStore.load(new FileInputStream(keystorename), claveKeystore.toCharArray());

            // Obtener la clave privada del servidor
            KeyStore.PasswordProtection keyPassword = new KeyStore.PasswordProtection(claveKeystore.toCharArray());
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(serverName, keyPassword);
            PrivateKey privateKey = privateKeyEntry.getPrivateKey();


            // Construir el token JWT

            return  Jwts.builder()
                    .setSubject(credentials.getUserName())
                    .claim(Constantes.ROLE, credentials.getRol())
                    .setExpiration(Date
                            .from(LocalDateTime.now().plusSeconds(access)
                                    .atZone(ZoneId.systemDefault()).toInstant()))
                    .signWith(privateKey)
                    .compact();
        } catch (CertificateException | KeyStoreException | IOException | NoSuchAlgorithmException |
                 UnrecoverableEntryException e) {
            Logger.getLogger(TokensGenerator.class.getName()).log(Level.SEVERE, null, e);
            throw new TokenInvalidoException(e.getMessage());

        }
    }

    public String getNewAccesTokenFromRefreshToken(String header) {
        try {
            // Cargar el keystore
            KeyStore keyStore = KeyStore.getInstance(Constantes.PKCS_12);
            keyStore.load(new FileInputStream(keystorename), claveKeystore.toCharArray());

            // Obtener la clave privada del servidor
            KeyStore.PasswordProtection keyPassword = new KeyStore.PasswordProtection(claveKeystore.toCharArray());
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(serverName, keyPassword);
            PrivateKey privateKey = privateKeyEntry.getPrivateKey();

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(privateKey)
                    .build()
                    .parseClaimsJws(header)
                    .getBody();
            String role = dao.findByUserName(claims.get(Constantes.USERNAME).toString()).orElseThrow().getRol();

            return  Jwts.builder()
                    .setSubject(claims.get(Constantes.USERNAME).toString())
                    .claim(Constantes.ROLE, role)
                    .setExpiration(Date
                            .from(LocalDateTime.now().plusSeconds(access)
                                    .atZone(ZoneId.systemDefault()).toInstant()))
                    .signWith(privateKey)

                    .compact();
        } catch (CertificateException | KeyStoreException | IOException | NoSuchAlgorithmException |
                 UnrecoverableEntryException e) {
            throw new TokenInvalidoException(e.getMessage());

        }
    }


    public String generateRefreshToken(Credentials credentials) {
        try {
            // Cargar el keystore
            KeyStore keyStore = KeyStore.getInstance(Constantes.PKCS_12);
            keyStore.load(new FileInputStream(keystorename), claveKeystore.toCharArray());

            // Obtener la clave privada del servidor
            KeyStore.PasswordProtection keyPassword = new KeyStore.PasswordProtection(claveKeystore.toCharArray());
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(serverName, keyPassword);
            PrivateKey privateKey = privateKeyEntry.getPrivateKey();


            // Construir el token JWT

            return Jwts.builder()
                    .setSubject(credentials.getUserName())
                    .claim(Constantes.USERNAME, credentials.getUserName())
                    .setExpiration(Date.from(LocalDateTime.now().plusMinutes(refreshExpiration)
                            .atZone(ZoneId.systemDefault()).toInstant()))
                    .claim(Constantes.ROLE, credentials.getRol())
                    .signWith(privateKey)

                    .compact();
        } catch (CertificateException | KeyStoreException | IOException | NoSuchAlgorithmException |
                 UnrecoverableEntryException e) {
            Logger.getLogger(TokensGenerator.class.getName()).log(Level.SEVERE, Constantes.ERROR_WHILE_PROCESSING_TOKENS, e);
            throw new TokenInvalidoException(Constantes.ERROR_WHILE_PROCESSING_TOKENS +e.getMessage());
        }
    }
}
