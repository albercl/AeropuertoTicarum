package dev.albercl.aeropuertoticarum.services;

import dev.albercl.aeropuertoticarum.model.Aerolinea;
import dev.albercl.aeropuertoticarum.model.Vuelo;
import dev.albercl.aeropuertoticarum.repositories.AerolineaRepository;
import dev.albercl.aeropuertoticarum.repositories.AvionRepository;
import dev.albercl.aeropuertoticarum.repositories.VueloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Component
public class AerolineaServices {

    private final AerolineaRepository aerolineaRepository;
    private final VueloRepository vueloRepository;
    private final AvionRepository avionRepository;

    @Autowired
    public AerolineaServices(AerolineaRepository aerolineaRepository, VueloRepository vueloRepository, AvionRepository avionRepository) {
        this.aerolineaRepository = aerolineaRepository;
        this.vueloRepository = vueloRepository;
        this.avionRepository = avionRepository;
    }

    public Aerolinea getAerolineaInfo(String aerolineaName) {
        Optional<Aerolinea> a = aerolineaRepository.getAerolineaByName(aerolineaName);

        if(a.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado la aerolínea con nombre: " + aerolineaName);

        return a.get();
    }

    public List<Vuelo> getVuelosPendientes(String aerolineaName) {
        if(aerolineaName.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La aerolínea especificada no es valida");
        }

        Optional<Aerolinea> a = aerolineaRepository.getAerolineaByName(aerolineaName);

        if(a.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La aerolínea proporcionada no existe");

        return vueloRepository.getVuelosPendientesByAerolinea(aerolineaName);
    }
}
