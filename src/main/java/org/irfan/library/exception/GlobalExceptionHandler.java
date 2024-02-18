package org.irfan.library.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Object> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        // Vous pouvez personnaliser la réponse comme vous le souhaitez
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    // Gestionnaires pour d'autres exceptions...
}
