package dev.albercl.aeropuertoticarum.services;

import dev.albercl.aeropuertoticarum.AeropuertoTicarum;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(
        classes = {AeropuertoTicarum.class}
)
@TestPropertySource(
        locations = "classpath:application-test.properties"
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AerolineaServiceTest {

    @Autowired
    private AerolineaService aerolineaService;

    @Autowired
    private AerolineaRepository aerolineaRepository;

    @Autowired
    private AvionRepository avionRepository;

    @Autowired
    private VueloRepository vueloRepository;

    private Aerolinea aerolinea;

    private Avion avion;

    @BeforeEach
    void resetDb() {
        vueloRepository.deleteAll();
        avionRepository.deleteAll();
        aerolineaRepository.deleteAll();

        aerolinea = new Aerolinea("aerolineaTest", 1L);
        aerolineaRepository.save(aerolinea);

        avion = new Avion("avionTest", 100, aerolinea);
        avionRepository.save(avion);
    }

    @Test
    void getAerolinea_InvalidAerolineaName_Null() {
        assertThrows(AerolineaNotFoundException.class, () -> aerolineaService.getAerolinea("invalidAerolineaName"));
        assertThrows(AerolineaNotFoundException.class, () -> aerolineaService.getAerolinea(""));
    }

    @Test
    void getAerolinea_ExistingAerolineaName_AerolineaObject() {
        assertEquals(
                aerolinea.getId(),
                aerolineaService.getAerolinea(aerolinea.getName()).getId()
        );
    }

    @Test
    void getVuelosPendientes_InvalidAerolineaName_Null() {
        assertEquals(0, aerolineaService.getVuelosPendientes("invalidAerolineaName").size());
        assertEquals(0, aerolineaService.getVuelosPendientes("").size());
    }

    @Test
    void getVuelosPendientes_ExistingAerolineaName_EmptyList() {
        assertEquals(0, aerolineaService.getVuelosPendientes(aerolinea.getName()).size());
    }

    @Test
    void getVuelosPendientes_WithVuelosPendientes_ListOfVuelos() {
        Vuelo v = new Vuelo(avion, aerolinea);
        v.setDespegue(LocalDateTime.now().plus(1, ChronoUnit.DAYS));
        vueloRepository.save(v);

        v = new Vuelo(avion, aerolinea);
        v.setDespegue(LocalDateTime.now().plus(1, ChronoUnit.MINUTES));
        vueloRepository.save(v);

        v = new Vuelo(avion, aerolinea);
        v.setDespegue(LocalDateTime.now().minus(1, ChronoUnit.DAYS));
        vueloRepository.save(v);

        assertEquals(2, aerolineaService.getVuelosPendientes(aerolinea.getName()).size());
    }

    @Test
    void addVuelo_InvalidAerolineaName_Exception() {
        Vuelo vuelo = new Vuelo(avion, null);
        assertThrows(AerolineaNotFoundException.class, () -> aerolineaService.addVuelo("invalidAerolineaName", vuelo));
        assertThrows(AerolineaNotFoundException.class, () -> aerolineaService.addVuelo("", vuelo));
    }

    @Test
    void addVuelo_WihtoutAvion_Exception() {
        Vuelo vuelo = new Vuelo(null, aerolinea);
        assertThrows(InvalidVueloException.class, () -> aerolineaService.addVuelo(aerolinea.getName(), vuelo));
    }

    @Test
    void addVuelo_WithAllData_VueloObject() {
        Vuelo vuelo = aerolineaService.addVuelo(aerolinea.getName(), new Vuelo(avion, aerolinea));

        assertNotNull(vuelo);
        assertNotNull(vuelo.getId());
        assertEquals(aerolinea.getId(), vuelo.getAerolinea().getId());
        assertEquals(avion.getId(), vuelo.getAvion().getId());
    }

    @Test
    void addVuelo_WithDespegue_Exception() {
        Vuelo vuelo = new Vuelo(avion, aerolinea);
        vuelo.setDespegue(LocalDateTime.now());
        assertThrows(InvalidVueloException.class, () -> aerolineaService.addVuelo(aerolinea.getName(), vuelo));
    }

    @Test
    void getVueloById_InvalidVueloId_VueloNotFoundException() {
        assertThrows(VueloNotFoundException.class, () -> aerolineaService.getVueloById(-1L));
    }

    @Test
    void getVueloById_ValidVueloId_VueloObject() {
        Vuelo vuelo = vueloRepository.save(new Vuelo(avion, aerolinea));

        assertEquals(vuelo.getId(), aerolineaService.getVueloById(vuelo.getId()).getId());
    }

    @Test
    void updateVuelo_InvalidVueloId_Exception() {
        Vuelo vuelo = new Vuelo(avion, aerolinea);
        assertThrows(VueloNotFoundException.class, () -> aerolineaService.updateVuelo(-1L, vuelo));
    }

    @Test
    void updateVuelo_VueloWithoutAvion_Exception() {
        Vuelo v = vueloRepository.save(new Vuelo(avion, aerolinea));
        Vuelo update = new Vuelo(null, aerolinea);

        assertThrows(InvalidVueloException.class, () -> aerolineaService.updateVuelo(v.getId(), update));
    }

    @Test
    void updateVuelo_WithInvalidAvion_Exception() {
        Vuelo v = vueloRepository.save(new Vuelo(avion, aerolinea));
        Avion av = new Avion();
        av.setId(-1L);
        Vuelo update = new Vuelo(av, aerolinea);

        assertThrows(InvalidVueloException.class, () -> aerolineaService.updateVuelo(v.getId(), update));
    }

    @Test
    void updateVuelo_ValidVuelo_VueloObject() {
        Vuelo v = vueloRepository.save(new Vuelo(avion, aerolinea));
        Vuelo update = new Vuelo(avion, aerolinea);

        assertEquals(v.getId(), aerolineaService.updateVuelo(v.getId(), update).getId());

        Vuelo updated = vueloRepository.findById(v.getId()).orElse(new Vuelo());

        assertEquals(v.getId(), updated.getId());
        assertEquals(v.getAerolinea().getId(), updated.getAerolinea().getId());
        assertEquals(v.getAvion().getId(), updated.getAvion().getId());
    }

    @Test
    void deleteVuelo_InvalidVueloId_Exception() {
        assertThrows(VueloNotFoundException.class, () -> aerolineaService.deleteVuelo(-1L));
    }

    @Test
    void deleteVuelo_ValidVuelo_VueloObject() {
        Vuelo v = vueloRepository.save(new Vuelo(avion, aerolinea));
        aerolineaService.deleteVuelo(v.getId());

        assertFalse(vueloRepository.existsById(v.getId()));
    }

    @Test
    void getVuelosDespegados_InvalidAerolineaName_Null() {
        assertEquals(0, aerolineaService.getVuelosDespegados("invalidAerolineaName").size());
        assertEquals(0, aerolineaService.getVuelosDespegados("").size());
    }

    @Test
    void getVuelosDespegados_ExistingAerolineaName_EmptyList() {
        assertEquals(0, aerolineaService.getVuelosDespegados(aerolinea.getName()).size());
    }

    @Test
    void getVuelosDespegados_WithVuelosDespegados_ListOfVuelos() {
        Vuelo v = new Vuelo(avion, aerolinea);
        v.setDespegue(LocalDateTime.now().minus(1, ChronoUnit.DAYS));
        vueloRepository.save(v);

        v = new Vuelo(avion, aerolinea);
        v.setDespegue(LocalDateTime.now().minus(1, ChronoUnit.MINUTES));
        vueloRepository.save(v);

        v = new Vuelo(avion, aerolinea);
        v.setDespegue(LocalDateTime.now().plus(1, ChronoUnit.DAYS));
        vueloRepository.save(v);

        assertEquals(2, aerolineaService.getVuelosDespegados(aerolinea.getName()).size());
    }

    @Test
    void despegarVuelo_InvalidVueloId_Exception() {
        assertThrows(VueloNotFoundException.class, () -> aerolineaService.despegarVuelo(-1L));
    }

    @Test
    void despegarVuelo_VueloDespegadoAnteriormente_Exception() {
        Vuelo v = new Vuelo(avion, aerolinea);
        v.setDespegue(LocalDateTime.now().minus(1, ChronoUnit.DAYS));
        vueloRepository.save(v);

        assertThrows(VueloAlreadyDespegadoException.class, () -> aerolineaService.despegarVuelo(v.getId()));
    }

    @Test
    void despegarVuelo_VueloNoDespegadoAnteriormente_VueloObject() {
        Vuelo v = new Vuelo(avion, aerolinea);
        vueloRepository.save(v);

        Vuelo despegado = aerolineaService.despegarVuelo(v.getId());

        assertNotNull(despegado.getDespegue());
        assertEquals(v.getId(), despegado.getId());
        assertEquals(v.getAerolinea().getId(), despegado.getAerolinea().getId());
        assertEquals(v.getAvion().getId(), despegado.getAvion().getId());
    }
}
