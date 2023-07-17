package com.evaluacion2023.controller;

import com.evaluacion2023.config.exceptions.InvalidRequestException;
import com.evaluacion2023.config.security.JwtToken;
import com.evaluacion2023.dto.UserDto;
import com.evaluacion2023.service.interfaces.LoginService;
import com.evaluacion2023.service.interfaces.UsuarioService;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@ControllerAdvice
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final LoginService loginService;

    private final JwtToken jwtToken;

    public UsuarioController(UsuarioService usuarioService, LoginService loginService, JwtToken jwtToken) {
        this.usuarioService = usuarioService;
        this.loginService = loginService;
        this.jwtToken = jwtToken;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Map<String, Object>> signUp(@RequestBody @Validated UserDto userDTO) {
            Map<String, Object> response = usuarioService.saveUser(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Validated @RequestHeader("Authorization") String authorizationHeader){
        String token = jwtToken.extractTokenFromAuthorizationHeader(authorizationHeader);
        Map<String, Object> response = loginService.loginUser(token);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }
}
