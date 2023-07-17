package com.evaluacion2023.config.exceptions;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CustomError {
    private LocalDateTime timestamp;
    private int codigo;
    private String detail;
}
