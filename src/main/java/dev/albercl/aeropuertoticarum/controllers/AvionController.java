package dev.albercl.aeropuertoticarum.controllers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/{aerolineaName}/services/salida")
public class AvionController {

    @GetMapping("/")
    public void getSalidas(@PathVariable String aerolineaName) {
        //TODO
    }

    @GetMapping("/{idVuelo}")
    public void getSalidaVuelo(@PathVariable String aerolineaName, @PathVariable long idVuelo) {
        //TODO
    }

    @PutMapping("/{idVuelo}/despegue")
    public void putDespegueVuelo(@PathVariable String aerolineaName, @PathVariable long idVuelo) {
        //TODO
    }
}
