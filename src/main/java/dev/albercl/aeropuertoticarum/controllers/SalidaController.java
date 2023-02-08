package dev.albercl.aeropuertoticarum.controllers;

import dev.albercl.aeropuertoticarum.dto.SalidaDto;
import dev.albercl.aeropuertoticarum.dto.VueloDto;
import dev.albercl.aeropuertoticarum.mappers.VueloMapper;
import dev.albercl.aeropuertoticarum.services.AerolineaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/{aerolineaName}/services/salida")
public class SalidaController {

    private final AerolineaService aerolineaService;
    private final VueloMapper vueloMapper;

    @Autowired
    public SalidaController(AerolineaService aerolineaService, VueloMapper vueloMapper) {
        this.aerolineaService = aerolineaService;
        this.vueloMapper = vueloMapper;
    }

    @GetMapping
    public List<VueloDto> getSalida(@PathVariable String aerolineaName) {
        return aerolineaService
                .getVuelosDespegados(aerolineaName)
                .stream()
                .map(vueloMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{idVuelo}")
    public SalidaDto getSalidaVuelo(@PathVariable long idVuelo) {
        return vueloMapper.toSalidaDto(aerolineaService.getVueloById(idVuelo));
    }

    @PostMapping("/{idVuelo}/despegue")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void postDespegueVuelo(@PathVariable long idVuelo) {
        aerolineaService.despegarVuelo(idVuelo);
    }
}
