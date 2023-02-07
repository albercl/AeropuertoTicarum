package dev.albercl.aeropuertoticarum.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "No se puede despegar un vuelo que ya ha depegado anteriomente")
public class VueloAlreadyDespegadoException extends RuntimeException {

    public VueloAlreadyDespegadoException(String message) {
        super(message);
    }
}
