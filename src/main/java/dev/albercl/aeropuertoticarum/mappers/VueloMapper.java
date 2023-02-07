package dev.albercl.aeropuertoticarum.mappers;

import dev.albercl.aeropuertoticarum.dto.VueloDto;
import dev.albercl.aeropuertoticarum.model.Vuelo;
import dev.albercl.aeropuertoticarum.repositories.AerolineaRepository;
import dev.albercl.aeropuertoticarum.repositories.AvionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VueloMapper {

    private final AerolineaRepository aerolineaRepository;
    private final AvionRepository avionRepository;

    @Autowired
    public VueloMapper(AerolineaRepository aerolineaRepository, AvionRepository avionRepository) {
        this.aerolineaRepository = aerolineaRepository;
        this.avionRepository = avionRepository;
    }

    public VueloDto toVueloDto(Vuelo entity) {
        VueloDto vdto = new VueloDto();

        vdto.setId(entity.getId());
        vdto.setAerolinea(entity.getAerolinea().getId());
        vdto.setAvion(entity.getAvion().getId());
        vdto.setDespegue(entity.getDespegue());

        return vdto;
    }

    public Vuelo toVuelo(VueloDto dto) {
        Vuelo v = new Vuelo();
        v.setId(dto.getId());
        v.setDespegue(dto.getDespegue());
        v.setAvion(avionRepository.findById(dto.getAvion()).orElse(null));
        v.setAerolinea(aerolineaRepository.findById(dto.getAerolinea()).orElse(null));

        return v;
    }
}
