package dev.albercl.aeropuertoticarum.services;

import dev.albercl.aeropuertoticarum.exceptions.AerolineaNotFoundException;
import dev.albercl.aeropuertoticarum.exceptions.InvalidVueloException;
import dev.albercl.aeropuertoticarum.exceptions.VueloAlreadyDespegadoException;
import dev.albercl.aeropuertoticarum.exceptions.VueloNotFoundException;
import dev.albercl.aeropuertoticarum.model.Aerolinea;
import dev.albercl.aeropuertoticarum.model.Avion;
import dev.albercl.aeropuertoticarum.model.Vuelo;
import dev.albercl.aeropuertoticarum.repositories.AerolineaRepository;
import dev.albercl.aeropuertoticarum.repositories.AvionRepository;
import dev.albercl.aeropuertoticarum.repositories.VueloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@Primary
public class AerolineaServiceImpl implements AerolineaService {
    private final AerolineaRepository aerolineaRepository;
    private final VueloRepository vueloRepository;
    private final AvionRepository avionRepository;

    @Autowired
    public AerolineaServiceImpl(AerolineaRepository aerolineaRepository, VueloRepository vueloRepository, AvionRepository avionRepository) {
        this.aerolineaRepository = aerolineaRepository;
        this.vueloRepository = vueloRepository;
        this.avionRepository = avionRepository;
    }

    @Override
    public Aerolinea getAerolinea(String aerolineaName) {
        Optional<Aerolinea> a = aerolineaRepository.findByName(aerolineaName);

        if (a.isEmpty())
            throw new AerolineaNotFoundException("La aerolínea no existe");

        return a.get();
    }

    @Override
    public List<Vuelo> getVuelosPendientes(String aerolineaName) {
        return vueloRepository.findPendientesByAerolinea(aerolineaName);
    }

    @Override
    public List<Avion> getAviones(String aerolineaName) {
        if(aerolineaName == null || aerolineaName.isBlank()) {
            throw new AerolineaNotFoundException("No se ha especificado la aerolínea");
        }

        return avionRepository.getByAerolinea(aerolineaName);
    }

    @Override
    public Vuelo addVuelo(String aerolineaName, Vuelo vuelo) {
        Optional<Aerolinea> aerolineaOptional = aerolineaRepository.findByName(aerolineaName);

        if (aerolineaOptional.isEmpty())
            throw new AerolineaNotFoundException("No se ha encontrado la aerolinea");

        vuelo.setAerolinea(aerolineaOptional.get());

        if (vuelo.getAvion() == null)
            throw new InvalidVueloException("No se ha especificado el avión o no se ha encontrado");

        if(!avionRepository.existsById(vuelo.getAvion().getId()))
            throw new InvalidVueloException("El avión no existe");

        if (vuelo.getDespegue() != null) {
            throw new InvalidVueloException("No se puede crear un vuelo con despegue");
        }

        return vueloRepository.save(vuelo);
    }

    @Override
    public Vuelo getVueloById(Long id) {
        return vueloRepository
                .findById(id)
                .orElseThrow(() -> new VueloNotFoundException("El vuelo con id " + id + " no existe"));
    }

    @Override
    public Vuelo updateVuelo(Long id, Vuelo vueloInput) {
        Vuelo vuelo = vueloRepository.findById(id).orElseThrow(() -> new VueloNotFoundException("El vuelo no existe"));

        if (vueloInput.getAvion() == null)
            throw new InvalidVueloException("El avión es obligatorio");

        // Actualizar nuevos valores del vuelo (solo se puede cambiar el avión)
        vuelo.setAvion(vueloInput.getAvion());

        try {
            return vueloRepository.save(vuelo);
        } catch (Exception e) {
            throw new InvalidVueloException("El avión no existe");
        }
    }

    @Override
    public void deleteVuelo(Long id) {
        if (!vueloRepository.existsById(id))
            throw new VueloNotFoundException("El vuelo no existe");

        vueloRepository.deleteById(id);
    }

    @Override
    public List<Vuelo> getVuelosDespegados(String aerolineaName) {
        if (aerolineaName.isBlank()) return new LinkedList<>();

        return vueloRepository.findDespegadosByAerolinea(aerolineaName);
    }

    @Override
    public Vuelo despegarVuelo(Long id) {
        Vuelo v = vueloRepository.findById(id).orElse(null);

        if (v == null)
            throw new VueloNotFoundException("El vuelo no existe");

        if (v.getDespegue() != null)
            throw new VueloAlreadyDespegadoException("El vuelo ya ha despegado");

        v.setDespegue(LocalDateTime.now());

        return vueloRepository.save(v);
    }
}
