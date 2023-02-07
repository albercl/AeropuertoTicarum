package dev.albercl.aeropuertoticarum.controllers;

import dev.albercl.aeropuertoticarum.dto.VueloDto;
import dev.albercl.aeropuertoticarum.mappers.VueloMapper;
import dev.albercl.aeropuertoticarum.model.Vuelo;
import dev.albercl.aeropuertoticarum.services.AerolineaServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/{aerolineaName}/services/vuelo")
public class VueloController {

    private AerolineaServices services;

    private VueloMapper vueloMapper;

    @Autowired
    public VueloController(AerolineaServices services, VueloMapper vueloMapper) {
        this.services = services;
        this.vueloMapper = vueloMapper;
    }


    @GetMapping
    public List<VueloDto> getVuelosPendientes(@PathVariable String aerolineaName) {
        return services.getVuelosPendientes(aerolineaName).stream()
                .map(v -> vueloMapper.toVueloDto(v))
                .collect(Collectors.toList());
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Vuelo postNuevoVuelo(@PathVariable String aerolineaName, @RequestBody VueloDto vuelo) {

        return null;
    }

    @GetMapping("/vuelo/{idVuelo}")
    public Vuelo getVuelo(@PathVariable String aerolineaName, @PathVariable long idVuelo) {
        //TODO
        throw new UnsupportedOperationException();
    }

    @PutMapping("/vuelo/{idVuelo}")
    public Vuelo putVuelo(@PathVariable String aerolineaName, @PathVariable long idVuelo) {
        //TODO
        throw new UnsupportedOperationException();
    }

    @DeleteMapping("/vuelo/{idVuelo}")
    public void deleteVuelo(@PathVariable String aerolineaName, @PathVariable long idVuelo) {
        //TODO
        throw new UnsupportedOperationException();
    }
}
