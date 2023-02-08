package dev.albercl.aeropuertoticarum.mappers;

import dev.albercl.aeropuertoticarum.dto.AvionDto;
import dev.albercl.aeropuertoticarum.model.Avion;
import org.springframework.stereotype.Component;

@Component
public class AvionMapper {

    public AvionDto toDto(Avion avion) {
        return new AvionDto(avion.getId(), avion.getModelo(), avion.getCapacidad());
    }
}
