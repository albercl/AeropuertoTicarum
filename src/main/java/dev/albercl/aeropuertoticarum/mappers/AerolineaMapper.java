package dev.albercl.aeropuertoticarum.mappers;

import dev.albercl.aeropuertoticarum.dto.AerolineaDto;
import dev.albercl.aeropuertoticarum.model.Aerolinea;
import org.springframework.stereotype.Component;

@Component
public class AerolineaMapper {

    public AerolineaDto toAerolineaDto(Aerolinea a) {
        AerolineaDto dto = new AerolineaDto();
        dto.setId(a.getId());
        dto.setNumeroAviones(a.getNumeroAviones());
        dto.setName(a.getName());

        return dto;
    }
}
