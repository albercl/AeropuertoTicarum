package dev.albercl.aeropuertoticarum.controllers;

import dev.albercl.aeropuertoticarum.dto.PostVueloDto;
import dev.albercl.aeropuertoticarum.dto.VueloDto;
import dev.albercl.aeropuertoticarum.mappers.VueloMapper;
import dev.albercl.aeropuertoticarum.model.Vuelo;
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
    @Operation(summary = "Get all the flights of the airline that have not yet taken off")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of flights"),
            @ApiResponse(responseCode = "404", description = "Airline not found")
    })
    public List<VueloDto> getVuelosPendientes(@PathVariable String aerolineaName) {
        return services.getVuelosPendientes(aerolineaName).stream()
                .map(vueloMapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new flight")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Flight created"),
            @ApiResponse(responseCode = "404", description = "Airline not found")
    })
    public VueloDto postNuevoVuelo(@PathVariable String aerolineaName, @RequestBody PostVueloDto vuelo) {
        return vueloMapper.toDto(
                services.addVuelo(
                        aerolineaName,
                        vueloMapper.toVuelo(vuelo)
                )
        );
    }

    @GetMapping("/{idVuelo}")
    @Operation(summary = "Get a flight by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Flight found"),
            @ApiResponse(responseCode = "404", description = "Flight not found")
    })
    public VueloDto getVuelo(@PathVariable String aerolineaName, @PathVariable long idVuelo) {
        return vueloMapper.toDto(services.getVueloById(idVuelo));
    }

    @PatchMapping("/{idVuelo}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update a flight")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Flight updated"),
            @ApiResponse(responseCode = "404", description = "Flight not found")
    })
    public VueloDto patchVuelo(@PathVariable String aerolineaName, @PathVariable long idVuelo, @RequestBody PostVueloDto vuelo) {
        Vuelo v = services.updateVuelo(idVuelo, vueloMapper.toVuelo(vuelo));
        return vueloMapper.toDto(v);
    }

    @DeleteMapping("/{idVuelo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a flight")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Flight deleted"),
            @ApiResponse(responseCode = "404", description = "Flight not found")
    })
    public void deleteVuelo(@PathVariable String aerolineaName, @PathVariable long idVuelo) {
        services.deleteVuelo(idVuelo);
    }
}
