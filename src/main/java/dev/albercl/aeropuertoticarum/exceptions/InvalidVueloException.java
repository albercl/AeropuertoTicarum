package dev.albercl.aeropuertoticarum.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Vuelo inválido")
public class InvalidVueloException extends RuntimeException {

    public InvalidVueloException(String message) {
        super(message);
    }
}
