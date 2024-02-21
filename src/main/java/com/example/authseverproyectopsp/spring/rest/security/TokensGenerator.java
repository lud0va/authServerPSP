package com.example.authseverproyectopsp.spring.rest.security;

import com.example.authseverproyectopsp.common.Configuration;
import com.example.authseverproyectopsp.common.Constantes;
import com.example.authseverproyectopsp.data.dao.CredentialsDao;
import com.example.authseverproyectopsp.domain.model.Credentials;
import com.example.authseverproyectopsp.spring.rest.errors.exceptions.TokenInvalidoException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
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
    private final CredentialsDao dao;

    public TokensGenerator(Configuration co, CredentialsDao dao) {
        this.co = co;
        this.dao = dao;
    }


    public String generateAccessToken(Credentials credentials) {
        try {
            // Cargar el keystore
            KeyStore keyStore = KeyStore.getInstance(Constantes.PKCS_12);
            keyStore.load(new FileInputStream(co.getNombreKeystore()), co.getClave().toCharArray());

            // Obtener la clave privada del servidor
            KeyStore.PasswordProtection keyPassword = new KeyStore.PasswordProtection(co.getClave().toCharArray());
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(co.getServerName(), keyPassword);
            PrivateKey privateKey = privateKeyEntry.getPrivateKey();


            // Construir el token JWT

            return  Jwts.builder()
                    .setSubject(credentials.getUserName())
                    .claim(Constantes.ROLE, credentials.getRol())
                    .setExpiration(Date
                            .from(LocalDateTime.now().plusSeconds(10)
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
            String role = dao.findByUserName(claims.get(Constantes.USERNAME).toString()).orElseThrow().getRol();

            return  Jwts.builder()
                    .setSubject(claims.get(Constantes.USERNAME).toString())
                    .claim(Constantes.ROLE, role)
                    .setExpiration(Date
                            .from(LocalDateTime.now().plusSeconds(60)
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
            keyStore.load(new FileInputStream(co.getNombreKeystore()), co.getClave().toCharArray());

            // Obtener la clave privada del servidor
            KeyStore.PasswordProtection keyPassword = new KeyStore.PasswordProtection(co.getClave().toCharArray());
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(co.getServerName(), keyPassword);
            PrivateKey privateKey = privateKeyEntry.getPrivateKey();


            // Construir el token JWT

            return Jwts.builder()
                    .setSubject(credentials.getUserName())
                    .claim(Constantes.USERNAME, credentials.getUserName())
                    .setExpiration(Date.from(LocalDateTime.now().plusMinutes(10)
                            .atZone(ZoneId.systemDefault()).toInstant()))
                    .claim(Constantes.ROLE, credentials.getRol())
                    .signWith(privateKey)

                    .compact();
        } catch (CertificateException | KeyStoreException | IOException | NoSuchAlgorithmException |
                 UnrecoverableEntryException e) {
            Logger.getLogger(TokensGenerator.class.getName()).log(Level.SEVERE, "Error while processing tokens", e);
            throw new TokenInvalidoException("Error while processing tokens"+e.getMessage());
        }
    }
}
