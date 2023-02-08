package dev.albercl.aeropuertoticarum;

import dev.albercl.aeropuertoticarum.model.Aerolinea;
import dev.albercl.aeropuertoticarum.model.Avion;
import dev.albercl.aeropuertoticarum.repositories.AerolineaRepository;
import dev.albercl.aeropuertoticarum.repositories.AvionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SampleDataLoader {
    private final AerolineaRepository aerolineaRepository;
    private final AvionRepository avionRepository;

    @Autowired
    public SampleDataLoader(AerolineaRepository aerolineaRepository,
                            AvionRepository avionRepository) {
        this.aerolineaRepository = aerolineaRepository;
        this.avionRepository = avionRepository;

        Aerolinea al = LoadAerolineas();
        LoadAviones(al);
    }

    private Aerolinea LoadAerolineas() {
        Aerolinea a = new Aerolinea();
        a.setName("MiAerolinea");

        return aerolineaRepository.save(a);
    }

    private void LoadAviones(Aerolinea al) {
        Avion av = new Avion();
        av.setModelo("Modelo 1");
        av.setCapacidad(100);
        av.setAerolinea(al);
        avionRepository.save(av);

        av = new Avion();
        av.setModelo("Modelo 2");
        av.setCapacidad(200);
        av.setAerolinea(al);
        avionRepository.save(av);

        av = new Avion();
        av.setModelo("Modelo 3");
        av.setCapacidad(300);
        av.setAerolinea(al);
        avionRepository.save(av);
    }
}
