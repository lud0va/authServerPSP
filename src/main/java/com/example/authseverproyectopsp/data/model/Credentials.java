package com.example.authseverproyectopsp.data.model;

import com.example.authseverproyectopsp.common.Constantes;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter

@AllArgsConstructor
@NoArgsConstructor
@Table(name = Constantes.CREDENTIALS)
public class Credentials {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = Constantes.ID, nullable = false)
    private int id;
    @Column(name = Constantes.USERNAME)
    private String userName;
    @Column(name = Constantes.PASSWORD)
    private String password;
    @Column(name = Constantes.ROL)
    private String rol;

    public Credentials(String userName, String password) {
        this.userName = userName;
        this.password = password;
        this.rol = Constantes.USER;
    }
}
