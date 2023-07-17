package com.evaluacion2023.config.exceptions;

import com.evaluacion2023.dto.UserDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ControllerAdvice
public class ExceptionHandlerController {

    private CustomError createCustomError(String message, HttpStatus status) {
        CustomError error = new CustomError();
        error.setTimestamp(LocalDateTime.now());
        error.setCodigo(status.value());
        error.setDetail(message);
        return error;
    }

    /**
     * Maneja excepciones genéricas y devuelve una respuesta con código de estado 500 (Internal Server Error).
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Maneja la excepción UserNotFoundException y devuelve una respuesta con código de estado 404 (Not Found).
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Maneja la excepción InvalidRequestException y devuelve una respuesta con código de estado 400 (Bad Request).
     */
    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<String> handleInvalidRequestException(InvalidRequestException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja excepciones de tipo UserAlreadyExistsException y devuelve una respuesta con código de estado 409 (Conflict).
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<List<CustomError>>handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        CustomError error = createCustomError(e.getMessage(), HttpStatus.CONFLICT);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Collections.singletonList(error));
    }

    /**
     * Maneja la excepción TokenRevokedException.
     * Retorna una respuesta HTTP con un cuerpo que contiene un único objeto CustomError en caso de algun problema
     * con la revocacion del token.
     */
    @ExceptionHandler(TokenRevokedException.class)
    public ResponseEntity<List<CustomError>>handleTokenRevokedException(TokenRevokedException e) {
        CustomError error = createCustomError(e.getMessage(), HttpStatus.FORBIDDEN);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections.singletonList(error));
    }

    /**
     * Maneja la excepción TokenErrorException.
     * Retorna una respuesta HTTP que indica el error en caso de intentar loguear con
     * un token en lista negra.
     */
    @ExceptionHandler(TokenErrorException.class)
    public ResponseEntity<List<CustomError>>handleTokenErrorException(TokenErrorException e) {
        CustomError error = createCustomError(e.getMessage(), HttpStatus.FORBIDDEN);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections.singletonList(error));
    }




    /**
     * Maneja la excepción MethodArgumentNotValidException y devuelve una respuesta con código de estado 400 (Bad Request).
     * Además, convierte los errores de validación en una lista de objetos CustomError. Se arma el custom error de forma
     * explicita por que el detonante de la exception esta en el DTO.
     * @see UserDto <h3>https://jakarta.ee/specifications/bean-validation/3.0/apidocs/jakarta/validation/constraints/package-summary.html</h3>
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<CustomError>> handleValidationException(MethodArgumentNotValidException ex) {
        List<CustomError> errors = new ArrayList<>();

        ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
            CustomError error = new CustomError();
            error.setTimestamp(LocalDateTime.now());
            error.setCodigo(400);
            error.setDetail(fieldError.getDefaultMessage());
            errors.add(error);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}