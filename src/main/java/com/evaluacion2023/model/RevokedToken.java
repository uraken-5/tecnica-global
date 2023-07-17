package com.evaluacion2023.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Entity
public class RevokedToken {
    @Id
    private String token;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}