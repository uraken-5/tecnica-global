package com.evaluacion2023.utils;

import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class FormatDateNow {
    public static LocalDateTime getActualDate(){
        LocalDateTime fechaHoraActual = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy h:mm:ss a");
        return fechaHoraActual;
    }
}
