package com.evaluacion2023.config.security;
import com.evaluacion2023.config.exceptions.InvalidAuthorizationHeaderException;
import com.evaluacion2023.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;


@Service
public class JwtToken {

    private final SecretKey secretKey;

    public JwtToken(){
        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    public String generateJwtToken(User user, SecretKey secretKey) {
        // Generar el token JWT utilizando el ID del usuario
        LocalDateTime expirationTime = LocalDateTime.now().plusHours(2); // Establecer el tiempo de expiración del token
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
        // Verificar que el encabezado de autorización no sea nulo o esté vacío
        if (authorizationHeader != null && !authorizationHeader.isEmpty()) {
            // Verificar que el encabezado comience con "Bearer"
            if (authorizationHeader.startsWith("Bearer ")) {
                // Extraer el token eliminando el prefijo "Bearer"
                return authorizationHeader.substring(7);
            }
        }

        // En caso de que el encabezado no cumpla con el formato esperado, puedes lanzar una excepción o manejarlo de acuerdo a tus necesidades
        throw new InvalidAuthorizationHeaderException("Encabezado de autorización inválido");
    }

    public SecretKey getSecretKey(){
        return this.secretKey;
    }


}
