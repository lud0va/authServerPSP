package com.example.authseverproyectopsp.spring.rest.security;


import com.example.authseverproyectopsp.common.Constantes;
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
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = false, jsr250Enabled = true)
public class MySecurity {

    private static final String[] AUTH_WHITE_LIST = {
            Constantes.V_3_API_DOCS,
            Constantes.SWAGGER_UI,
            Constantes.V_2_API_DOCS,
            Constantes.SWAGGER_RESOURCES
    };


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http


                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers(Constantes.LOGIN).permitAll()
                                .requestMatchers(Constantes.REGISTER).permitAll()
                                .requestMatchers(Constantes.GETACCESSTOKEN).permitAll()
                                .requestMatchers(AUTH_WHITE_LIST).permitAll()
                                .anyRequest().authenticated()


                ).build();
    }


}
