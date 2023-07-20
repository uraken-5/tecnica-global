package com.evaluacion2023.dto;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Getter
@Setter
public class UserDto {
    private String name;
    @Pattern(regexp = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", message = "El correo no cumple con el formato")
    private String email;
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d.*\\d)(?=.*[a-z])[A-Za-z0-9]{8,12}$", message = "password no valida")
    private String password;
	@NotNull
    private List<PhoneDto> phones;
}