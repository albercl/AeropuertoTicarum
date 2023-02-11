package dev.albercl.aeropuertoticarum.controllers.handlers;

import dev.albercl.aeropuertoticarum.exceptions.AerolineaNotFoundException;
import dev.albercl.aeropuertoticarum.exceptions.InvalidVueloException;
import dev.albercl.aeropuertoticarum.exceptions.VueloAlreadyDespegadoException;
import dev.albercl.aeropuertoticarum.exceptions.VueloNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class AerolineaExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({AerolineaNotFoundException.class, InvalidVueloException.class, VueloAlreadyDespegadoException.class, VueloNotFoundException.class})
    protected ResponseEntity<Object> handleExceptions(ResponseStatusException ex) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), ex.getStatus(), null);
    }
}
