package com.example.authseverproyectopsp.common;

import com.example.authseverproyectopsp.data.dao.CredentialsDao;
import com.example.authseverproyectopsp.security.CustomUserDetailsService;
import lombok.Getter;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.Properties;

@org.springframework.context.annotation.Configuration
@Getter
public class Configuration {

    private String clave;
    private String nombreKeystore;
    private String serverName;


    public Configuration() {
        try {
            Properties p = new Properties();
            p.loadFromXML(Configuration.class.getClassLoader().getResourceAsStream("config/claveKeystore.xml"));
            this.clave = p.getProperty(Constantes.CLAVESERV);
            this.nombreKeystore = p.getProperty(Constantes.KEYSTORENAME);
            this.serverName = p.getProperty(Constantes.SERVERNAME);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public UserDetailsService userDetailsService(CredentialsDao userRepository) {
        return new CustomUserDetailsService(userRepository);
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService,
                                                         CredentialsDao userRepository,
                                                         PasswordEncoder encoder) {
        var dao = new DaoAuthenticationProvider();
        dao.setUserDetailsService(userDetailsService);
        dao.setPasswordEncoder(encoder);
        return dao;
    }

    @Bean
    public PasswordEncoder createPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }
}
