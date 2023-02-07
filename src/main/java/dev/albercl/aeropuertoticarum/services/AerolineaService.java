package dev.albercl.aeropuertoticarum.services;

import dev.albercl.aeropuertoticarum.model.Aerolinea;
import dev.albercl.aeropuertoticarum.model.Vuelo;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface AerolineaService {
    Aerolinea getAerolinea(String aerolineaName);

    List<Vuelo> getVuelosPendientes(String aerolineaName);

    Vuelo addVuelo(String aerolineaName, Vuelo vuelo);

    Vuelo getVueloById(Long id);

    Vuelo updateVuelo(Long id, Vuelo vuelo);

    void deleteVuelo(Long id);

    List<Vuelo> getVuelosDespegados(String aerolineaName);

    Vuelo despegarVuelo(Long id);
}
