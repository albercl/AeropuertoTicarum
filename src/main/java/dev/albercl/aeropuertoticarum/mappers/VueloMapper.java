package dev.albercl.aeropuertoticarum.mappers;

import dev.albercl.aeropuertoticarum.dto.PostVueloDto;
import dev.albercl.aeropuertoticarum.dto.SalidaDto;
import dev.albercl.aeropuertoticarum.dto.VueloDto;
import dev.albercl.aeropuertoticarum.model.Vuelo;
import dev.albercl.aeropuertoticarum.repositories.AvionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VueloMapper {

    private final AvionRepository avionRepository;

    @Autowired
    public VueloMapper(AvionRepository avionRepository) {
        this.avionRepository = avionRepository;
    }

    public VueloDto toDto(Vuelo entity) {
        VueloDto vdto = new VueloDto();

        vdto.setId(entity.getId());
        vdto.setAerolinea(entity.getAerolinea().getId());
        vdto.setAvion(entity.getAvion().getId());
        vdto.setDespegue(entity.getDespegue());
        vdto.setEntrada(entity.getEntrada());

        return vdto;
    }

    public SalidaDto toSalidaDto(Vuelo entity) {
        return new SalidaDto(entity.getId(), entity.getDespegue() != null, entity.getDespegue());
    }

    public Vuelo toVuelo(PostVueloDto dto) {
        Vuelo v = new Vuelo();
        v.setAvion(avionRepository.findById(dto.getAvion()).orElse(null));

        return v;
    }
}
