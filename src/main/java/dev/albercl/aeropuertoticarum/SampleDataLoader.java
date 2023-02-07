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

        LoadAerolineas();
        LoadAviones();
    }

    private void LoadAerolineas() {
        Aerolinea a = new Aerolinea();
        a.setName("MiAerolinea");

        aerolineaRepository.save(a);
    }

    private void LoadAviones() {
        Avion av = new Avion();
        av.setModel("Modelo 1");
        av.setCapacity(100);
        avionRepository.save(av);

        av = new Avion();
        av.setModel("Modelo 2");
        av.setCapacity(200);
        avionRepository.save(av);

        av = new Avion();
        av.setModel("Modelo 3");
        av.setCapacity(300);
        avionRepository.save(av);
    }
}
