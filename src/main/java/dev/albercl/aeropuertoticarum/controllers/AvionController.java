package dev.albercl.aeropuertoticarum.controllers;

import dev.albercl.aeropuertoticarum.dto.AvionDto;
import dev.albercl.aeropuertoticarum.mappers.AvionMapper;
import dev.albercl.aeropuertoticarum.services.AerolineaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/{aerolineaName}/services/aviones")
public class AvionController {

    private final AerolineaService service;

    private final AvionMapper mapper;

    @Autowired
    public AvionController(AerolineaService service, AvionMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public List<AvionDto> getAviones(@PathVariable String aerolineaName) {
        return service.getAviones(aerolineaName)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}
