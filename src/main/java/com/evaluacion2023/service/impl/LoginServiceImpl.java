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

    /**
     * Realiza el inicio de sesión de un usuario utilizando un token JWT.
     *
     * @param token El token JWT proporcionado por el cliente al iniciar sesión.
     * @return Un mapa con la información del usuario y el nuevo token JWT generado.
     * @throws TokenErrorException Si el token es inválido o ha expirado.
     * @throws TokenRevokedException Si el token anterior ha sido revocado y es inválido.
     * @throws UserNotFoundException Si no se encuentra el usuario asociado al token.
     */
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
        String newToken = jwtToken.generateJwtToken(user, jwtToken.getSecretKey());
        revokeToken(token, user);

        Map<String, Object> response = getResponse(user);
        response.put("token", newToken);
        return response;
    }

    /**
     * Obtiene el ID del usuario a partir del token JWT proporcionado.
     *
     * @param token El token JWT proporcionado por el cliente.
     * @return El ID del usuario extraído del token.
     * @throws TokenErrorException Si el token es inválido o ha expirado.
     */
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

    /**
     * Construye un mapa con la información del usuario para ser devuelto en la respuesta.
     *
     * @param user El objeto User que contiene la información del usuario.
     * @return Un mapa con los detalles del usuario.
     */
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


    /**
     * Verifica si el token proporcionado ha sido revocado para el usuario dado.
     *
     * @param token El token JWT proporcionado por el cliente.
     * @param user  El objeto User asociado al token.
     * @return true si el token está revocado para el usuario, false de lo contrario.
     */
    private boolean isTokenRevoked(String token, User user) {
        List<RevokedToken> revokedTokens = user.getRevokedTokens();
        return revokedTokens.stream().anyMatch(t -> t.getToken().equals(token));
    }


    /**
     * Revoca el token anterior del usuario y guarda el nuevo token generado.
     *
     * @param token El token JWT anterior que se debe revocar.
     * @param user  El objeto User al que se asocia el token.
     */
    private void revokeToken(String token, User user) {
        RevokedToken revokedToken = new RevokedToken();
        revokedToken.setToken(token);
        revokedToken.setUser(user);
        user.getRevokedTokens().add(revokedToken);
        userRepository.save(user);
    }
}
