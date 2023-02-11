package dev.albercl.aeropuertoticarum.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No se ha encontrado la aerolinea solicitada")
public class AerolineaNotFoundException extends RuntimeException {
    public AerolineaNotFoundException(String message) {
        super(message);
    }
}
