package com.example.authseverproyectopsp.common;

import com.example.authseverproyectopsp.data.dao.CredentialsDao;
import com.example.authseverproyectopsp.spring.rest.security.CustomUserDetailsService;
import lombok.Getter;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;



@org.springframework.context.annotation.Configuration
@Getter
public class Configuration {




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
