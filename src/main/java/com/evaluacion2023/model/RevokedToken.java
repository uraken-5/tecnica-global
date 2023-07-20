package com.evaluacion2023.model;

import javax.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
public class RevokedToken {
    @Id
    private String token;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}