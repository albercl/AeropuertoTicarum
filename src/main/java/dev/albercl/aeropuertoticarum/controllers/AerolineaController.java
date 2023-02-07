package dev.albercl.aeropuertoticarum.controllers;

import dev.albercl.aeropuertoticarum.dto.AerolineaDto;
import dev.albercl.aeropuertoticarum.mappers.AerolineaMapper;
import dev.albercl.aeropuertoticarum.services.AerolineaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public AerolineaDto getAerolineaInfo(@PathVariable String aerolineaName) {
        return aerolineaMapper.toAerolineaDto(
                services.getAerolinea(aerolineaName)
        );
    }
}
