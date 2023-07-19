package com.evaluacion2023.config.security;

import com.evaluacion2023.config.exceptions.InvalidAuthorizationHeaderException;
import com.evaluacion2023.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;


@Service
public class JwtToken {

    private final SecretKey secretKey;

    public JwtToken(){
        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    public String generateJwtToken(User user, SecretKey secretKey) {
        LocalDateTime expirationTime = LocalDateTime.now().plusHours(2);
        String token = Jwts.builder()
                .setSubject(user.getId().toString())
                .claim("name",user.getName())
                .claim("email",user.getEmail())
                .setIssuedAt(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(expirationTime.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(secretKey)
                .compact();

        return token;
    }

    public String extractTokenFromAuthorizationHeader(String authorizationHeader) {
        if (authorizationHeader != null && !authorizationHeader.isEmpty()) {
            if (authorizationHeader.startsWith("Bearer ")) {
                return authorizationHeader.substring(7);
            }
        }
        throw new InvalidAuthorizationHeaderException("Encabezado de autorización inválido");
    }

    public SecretKey getSecretKey(){
        return this.secretKey;
    }


}
