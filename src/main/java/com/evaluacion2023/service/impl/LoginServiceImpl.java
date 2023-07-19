package com.evaluacion2023.service.impl;

import com.evaluacion2023.config.exceptions.TokenErrorException;
import com.evaluacion2023.config.exceptions.TokenRevokedException;
import com.evaluacion2023.config.exceptions.UserNotFoundException;
import com.evaluacion2023.config.security.JwtToken;
import com.evaluacion2023.model.Phone;
import com.evaluacion2023.model.RevokedToken;
import com.evaluacion2023.model.User;
import com.evaluacion2023.repository.UserRepository;
import com.evaluacion2023.service.interfaces.LoginService;
import com.evaluacion2023.config.exceptions.CustomError;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LoginServiceImpl implements LoginService {

    private final UserRepository userRepository;
    private final JwtToken jwtToken;

    public LoginServiceImpl(UserRepository userRepository, JwtToken jwtToken) {
        this.userRepository = userRepository;
        this.jwtToken = jwtToken;
    }
    @Override
    public Map<String, Object> loginUser(String token) {
        UUID userId = getUserIdFromToken(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

        if (isTokenRevoked(token, user)) {
            throw new TokenRevokedException("El token anterior es inválido");
        }

        // Actualiza el token para el usuario
        user.setId(user.getId());
        userRepository.save(user);
        String newToken = jwtToken.generateJwtToken(user,jwtToken.getSecretKey());
        revokeToken(token, user);

        Map<String, Object> response = getResponse(user);
        response.put("token", newToken);
        return response;
    }

    public UUID getUserIdFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(jwtToken.getSecretKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String userIdString = claims.getSubject();
            return UUID.fromString(userIdString);
        } catch (Exception e) {
            throw new TokenErrorException("Token inválido o expirado");
        }
    }

    private Map<String, Object> getResponse(User user) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("id", user.getId().toString());
        response.put("created", user.getCreated());
        response.put("lastLogin", user.getLastLogin());
        response.put("isActive", user.isActive());
        response.put("name", user.getName());
        response.put("email", user.getEmail());
        response.put("password", user.getPassword());
        List<Map<String, Object>> phoneList = new ArrayList<>();
        for (Phone phone : user.getPhones()) {
            Map<String, Object> phoneData = new LinkedHashMap<>();
            phoneData.put("number", phone.getNumber());
            phoneData.put("cityCode", phone.getCityCode());
            phoneData.put("countryCode", phone.getCountryCode());
            phoneList.add(phoneData);
        }
        response.put("phones", phoneList);
        return response;
    }

    private boolean isTokenRevoked(String token, User user) {
        List<RevokedToken> revokedTokens = user.getRevokedTokens();
        return revokedTokens.stream().anyMatch(t -> t.getToken().equals(token));
    }

    private void revokeToken(String token, User user) {
        RevokedToken revokedToken = new RevokedToken();
        revokedToken.setToken(token);
        revokedToken.setUser(user);
        user.getRevokedTokens().add(revokedToken);
        userRepository.save(user);
    }

}
