package dev.albercl.aeropuertoticarum.controllers;

import dev.albercl.aeropuertoticarum.dto.AerolineaDto;
import dev.albercl.aeropuertoticarum.mappers.AerolineaMapper;
import dev.albercl.aeropuertoticarum.services.AerolineaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/{aerolineaName}/services")
public class AerolineaController {

    private final AerolineaMapper aerolineaMapper;
    private final AerolineaService services;

    @Autowired
    public AerolineaController(AerolineaMapper aerolineaMapper, AerolineaService services) {
        this.aerolineaMapper = aerolineaMapper;
        this.services = services;
    }

    @GetMapping("/info")
    @Operation(summary = "Get information about the airline")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Information about the airline"),
            @ApiResponse(responseCode = "404", description = "Airline not found")
    })
    public AerolineaDto getAerolineaInfo(@PathVariable String aerolineaName) {
        return aerolineaMapper.toAerolineaDto(
                services.getAerolinea(aerolineaName)
        );
    }
}
