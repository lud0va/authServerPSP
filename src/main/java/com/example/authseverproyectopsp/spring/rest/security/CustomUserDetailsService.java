package com.example.authseverproyectopsp.spring.rest.security;


import com.example.authseverproyectopsp.common.Constantes;
import com.example.authseverproyectopsp.data.dao.CredentialsDao;
import com.example.authseverproyectopsp.domain.model.Credentials;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {


    private final CredentialsDao userRepository;

    public CustomUserDetailsService(CredentialsDao userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Credentials user = userRepository.findByUserName(username).orElseThrow(() -> new UsernameNotFoundException(Constantes.USER_NOT_FOUND));


        return User.builder()
                .username(user.getUserName())
                .password(user.getPassword())
                .roles(
                        user.getRol())
                .build();

    }
}
