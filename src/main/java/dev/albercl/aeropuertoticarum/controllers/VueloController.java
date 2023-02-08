package dev.albercl.aeropuertoticarum.controllers;

import dev.albercl.aeropuertoticarum.dto.PostVueloDto;
import dev.albercl.aeropuertoticarum.dto.VueloDto;
import dev.albercl.aeropuertoticarum.mappers.VueloMapper;
import dev.albercl.aeropuertoticarum.model.Vuelo;
import dev.albercl.aeropuertoticarum.services.AerolineaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/{aerolineaName}/services/vuelo")
public class VueloController {

    private final AerolineaService services;

    private final VueloMapper vueloMapper;

    @Autowired
    public VueloController(AerolineaService services, VueloMapper vueloMapper) {
        this.services = services;
        this.vueloMapper = vueloMapper;
    }


    @GetMapping
    public List<VueloDto> getVuelosPendientes(@PathVariable String aerolineaName) {
        return services.getVuelosPendientes(aerolineaName).stream()
                .map(vueloMapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public VueloDto postNuevoVuelo(@PathVariable String aerolineaName, @RequestBody PostVueloDto vuelo) {
        return vueloMapper.toDto(
                services.addVuelo(
                        aerolineaName,
                        vueloMapper.toVuelo(vuelo)
                )
        );
    }

    @GetMapping("/{idVuelo}")
    public VueloDto getVuelo(@PathVariable long idVuelo) {
        return vueloMapper.toDto(services.getVueloById(idVuelo));
    }

    @PatchMapping("/{idVuelo}")
    @ResponseStatus(HttpStatus.OK)
    public VueloDto patchVuelo(@PathVariable long idVuelo, @RequestBody PostVueloDto vuelo) {
        Vuelo v = services.updateVuelo(idVuelo, vueloMapper.toVuelo(vuelo));
        return vueloMapper.toDto(v);
    }

    @DeleteMapping("/{idVuelo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVuelo(@PathVariable long idVuelo) {
        services.deleteVuelo(idVuelo);
    }
}
