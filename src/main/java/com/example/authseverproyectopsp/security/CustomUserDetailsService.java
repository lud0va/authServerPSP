package com.example.authseverproyectopsp.security;


import com.example.authseverproyectopsp.data.dao.CredentialsDao;
import com.example.authseverproyectopsp.data.model.Credentials;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {


    private final CredentialsDao userRepository;

    public CustomUserDetailsService(CredentialsDao userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Credentials user = userRepository.findByUserName(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));


        return User.builder()
                .username(user.getUserName())
                .password(user.getPassword())
                .roles(
                        user.getRol())
                .build();

    }
}
