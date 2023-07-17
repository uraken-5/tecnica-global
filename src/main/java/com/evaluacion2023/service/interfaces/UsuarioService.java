package com.evaluacion2023.service.interfaces;


import com.evaluacion2023.dto.UserDto;

import java.util.Map;

@FunctionalInterface
public interface UsuarioService {
    Map<String, Object> saveUser(UserDto userDTO);
}
