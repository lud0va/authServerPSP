package com.example.authseverproyectopsp.spring;

import com.example.authseverproyectopsp.common.Configuration;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.x509.X509V3CertificateGenerator;

import javax.security.auth.x500.X500Principal;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class CrearKeystore {
    public static void main(String[] args) {
        try {
            //dejo el crear keystore por si necesitas crear la keystore otra vez para ver si lo hago bien o alguna cosa así
            Security.addProvider(new BouncyCastleProvider());
            Configuration c = new Configuration();
            String s = c.getClave();
            //crear claves privadas
            KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
            KeyPair claveBase64 = gen.generateKeyPair();
            PrivateKey clavePriv = claveBase64.getPrivate();
            PublicKey clavePub = claveBase64.getPublic();
            //iniciar keystore
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(null, null);



            //certificar clave publica
            X509V3CertificateGenerator cert1 = new X509V3CertificateGenerator();
            cert1.setSerialNumber(BigInteger.valueOf(1));
            cert1.setIssuerDN(new X500Principal("CN="+c.getServerName()));
            cert1.setSubjectDN(new X500Principal("CN="+c.getServerName()));
            cert1.setPublicKey(clavePub);
            cert1.setNotBefore(
                    Date.from(LocalDate.now().plus(365, ChronoUnit.DAYS).atStartOfDay().toInstant(ZoneOffset.UTC)));
            cert1.setNotAfter(new Date());
            cert1.setSignatureAlgorithm("SHA256WithRSAEncryption");
            X509Certificate cert =cert1.generateX509Certificate(clavePriv);


            // meter la claves del servidor
            keyStore.load(null, null);
            keyStore.setCertificateEntry(c.getServerName(), cert);
            keyStore.setKeyEntry(c.getServerName(), clavePriv, c.getClave().toCharArray(), new java.security.cert.Certificate[]{cert});

            keyStore.store(new FileOutputStream(c.getNombreKeystore()), s.toCharArray());

        } catch (KeyStoreException | CertificateException | IOException | NoSuchAlgorithmException |
                 InvalidKeyException | SignatureException e) {
            throw new IllegalStateException(e);
        }
    }
}
