package com.evaluacion2023.model;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;
    private String name;
    private String email;
    private String password;
    private LocalDateTime created;
    private LocalDateTime lastLogin;
    private boolean isActive;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Phone> phones;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<RevokedToken> revokedTokens;

    // Constructor, getters y setters
}
