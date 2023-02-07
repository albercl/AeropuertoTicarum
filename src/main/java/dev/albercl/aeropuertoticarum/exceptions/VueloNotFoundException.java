package dev.albercl.aeropuertoticarum.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Vuelo no encontrado")
public class VueloNotFoundException extends RuntimeException {
    public VueloNotFoundException(String message) {
        super(message);
    }
}
