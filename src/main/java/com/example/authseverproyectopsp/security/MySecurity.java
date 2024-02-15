package com.example.authseverproyectopsp.security;



import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true,prePostEnabled = false,jsr250Enabled = true)
public class MySecurity {

    private static final String[] AUTH_WHITE_LIST = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/v2/api-docs/**",
            "/swagger-resources/**"
    };








    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http


                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/login").permitAll()
                                .requestMatchers("/register").permitAll()
                                .requestMatchers("/getAccessToken").permitAll()
                                .requestMatchers("/swagger-ui/**").permitAll()
                                .requestMatchers(AUTH_WHITE_LIST).permitAll()
                                .anyRequest().authenticated()


                ).build();
    }



}
