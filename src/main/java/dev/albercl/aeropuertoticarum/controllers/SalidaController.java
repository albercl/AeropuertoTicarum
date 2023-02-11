package dev.albercl.aeropuertoticarum.controllers;

import dev.albercl.aeropuertoticarum.dto.SalidaDto;
import dev.albercl.aeropuertoticarum.dto.VueloDto;
import dev.albercl.aeropuertoticarum.mappers.VueloMapper;
import dev.albercl.aeropuertoticarum.services.AerolineaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Get all the flights that have taken off")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of flights"),
            @ApiResponse(responseCode = "404", description = "Airline not found")
    })
    public List<VueloDto> getSalida(@PathVariable String aerolineaName) {
        return aerolineaService
                .getVuelosDespegados(aerolineaName)
                .stream()
                .map(vueloMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{idVuelo}")
    @Operation(summary = "Get the info about a flight status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Flight")
    })
    public SalidaDto getSalidaVuelo(@PathVariable String aerolineaName, @PathVariable long idVuelo) {
        return vueloMapper.toSalidaDto(aerolineaService.getVueloById(idVuelo));
    }

    @PostMapping("/{idVuelo}/despegue")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Post the flight that has taken off")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Flight")
    })
    public void postDespegueVuelo(@PathVariable String aerolineaName, @PathVariable long idVuelo) {
        aerolineaService.despegarVuelo(idVuelo);
    }
}
