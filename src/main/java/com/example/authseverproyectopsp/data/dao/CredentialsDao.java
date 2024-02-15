package com.example.authseverproyectopsp.data.dao;

import com.example.authseverproyectopsp.data.model.Credentials;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CredentialsDao extends ListCrudRepository<Credentials, Long> {

    Optional<Credentials> findByUserName(String userName);

}
