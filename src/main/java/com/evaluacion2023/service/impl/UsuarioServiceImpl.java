package com.evaluacion2023.service.impl;

import com.evaluacion2023.config.exceptions.UserAlreadyExistsException;
import com.evaluacion2023.config.security.JwtToken;
import com.evaluacion2023.dto.UserDto;
import com.evaluacion2023.model.Phone;
import com.evaluacion2023.model.User;
import com.evaluacion2023.repository.UserRepository;
import com.evaluacion2023.service.interfaces.UsuarioService;
import com.evaluacion2023.utils.FormatDateNow;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UserRepository userRepository;
    private final JwtToken jwtToken;
    private final ModelMapper modelMapper;
    private final FormatDateNow formatDateNow;
    private final MessageSource messageSource;

    public UsuarioServiceImpl(UserRepository userRepository, ModelMapper modelMapper, JwtToken jwtToken, MessageSource messageSource, FormatDateNow formatDateNow) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.jwtToken = jwtToken;
        this.messageSource = messageSource;
        this.formatDateNow = formatDateNow;
    }

    /**
     * Guarda un usuario en la base de datos.
     *
     * @param userDTO El DTO del usuario.
     * @return Una respuesta con el usuario guardado.
     */
    @Override
    public Map<String, Object> saveUser(UserDto userDTO) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        User user = modelMapper.map(userDTO, User.class);
        user.setPassword(encoder.encode(userDTO.getPassword()));
        user.setCreated(FormatDateNow.getActualDate());
        user.setLastLogin(FormatDateNow.getActualDate());
        user.setActive(true);

        List<Phone> phoneList = userDTO.getPhones().stream()
                .map(phoneDto -> {
                    Phone phoneEntity = modelMapper.map(phoneDto, Phone.class);
                    phoneEntity.setUser(user);
                    return phoneEntity;
                }).collect(Collectors.toList());
        user.setPhones(phoneList);

        if (userExists(user.getEmail())) {
            throw new UserAlreadyExistsException(messageSource.getMessage("user.alreadyExists", null, null));
        }

        userRepository.save(user);
        Map<String, Object> response = getResponse(user);
        return response;
    }

    private boolean userExists(String email) {
        User user = userRepository.findByEmail(email);
        return user != null;
    }

    private Map<String, Object> getResponse(User user) {
        String token = jwtToken.generateJwtToken(user, jwtToken.getSecretKey());
        Map<String, Object> response = new HashMap<>();
        response.put(messageSource.getMessage("user.response.id", null, null), user.getId());
        response.put(messageSource.getMessage("user.response.created", null, null), LocalDateTime.now());
        response.put(messageSource.getMessage("user.response.lastLogin", null, null), LocalDateTime.now());
        response.put(messageSource.getMessage("user.response.token", null, null), token);
        response.put(messageSource.getMessage("user.response.active", null, null), true);
        return response;
    }
}
