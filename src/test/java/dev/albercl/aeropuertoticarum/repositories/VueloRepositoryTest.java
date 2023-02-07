package dev.albercl.aeropuertoticarum.repositories;

import dev.albercl.aeropuertoticarum.AeropuertoTicarum;
import dev.albercl.aeropuertoticarum.model.Aerolinea;
import dev.albercl.aeropuertoticarum.model.Avion;
import dev.albercl.aeropuertoticarum.model.Vuelo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(
        classes = {AeropuertoTicarum.class}
)
@TestPropertySource(
        locations = "classpath:application-test.properties"
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class VueloRepositoryTest {
    @Autowired
    private VueloRepository vueloRepository;

    @Autowired
    private AerolineaRepository aerolineaRepository;

    @Autowired
    private AvionRepository avionRepository;

    private Avion av;
    private Aerolinea al;

    @BeforeAll
    void initData() {
        al = new Aerolinea("aerolineaTest", 1L);
        av = new Avion("avionTest", 100, al);

        aerolineaRepository.save(al);
        avionRepository.save(av);
    }

    @BeforeEach
    void clearDb() {
        vueloRepository.deleteAll();
    }

    @Test
    void findPendientesByAerolinea_NoVuelos_EmptyList() {
        List<Vuelo> results = vueloRepository.findPendientesByAerolinea(al.getName());
        assertEquals(0, results.size());
    }

    @Test
    void findPendientesByAerolinea_VuelosDespegados_EmptyList() {
        Vuelo v = new Vuelo(av, al);
        v.setDespegue(LocalDateTime.now().minusMinutes(1));
        vueloRepository.save(v);

        List<Vuelo> results = vueloRepository.findPendientesByAerolinea(al.getName());
        assertEquals(0, results.size());
    }

    @Test
    void findPendientesByAerolinea_SomeVuelos_ListWithJustVuelosPendientes() {
        List<Vuelo> expected = new LinkedList<>();

        // Shouldn't be listed
        Vuelo v = new Vuelo(av, al);
        v.setDespegue(LocalDateTime.now().minusMinutes(1));
        vueloRepository.save(v);

        // Should be listed
        v = new Vuelo(av, al);
        v.setDespegue(LocalDateTime.now().plusMinutes(1));
        expected.add(vueloRepository.save(v));

        expected.add(vueloRepository.save(new Vuelo(av, al)));

        List<Long> expectedIds = expected.stream()
                .map(Vuelo::getId)
                .collect(Collectors.toList());

        List<Vuelo> results = vueloRepository.findPendientesByAerolinea(al.getName());
        assertEquals(2, results.size());
        results.stream()
                .map(Vuelo::getId)
                .forEach(id -> assertTrue(expectedIds.contains(id)));
    }

    @Test
    void findDespegadosByAerolinea_NoVuelos_EmptyList() {
        List<Vuelo> results = vueloRepository.findDespegadosByAerolinea(al.getName());
        assertEquals(0, results.size());
    }

    @Test
    void findDespegadosByAerolinea_VuelosPendientes_EmptyList() {
        Vuelo v = new Vuelo(av, al);
        v.setDespegue(LocalDateTime.now().plusMinutes(1));
        vueloRepository.save(v);

        List<Vuelo> results = vueloRepository.findDespegadosByAerolinea(al.getName());
        assertEquals(0, results.size());
    }

    @Test
    void findDespegadosByAerolinea_SomeVuelos_ListWithJustVuelosDespegados() {
        List<Vuelo> expected = new LinkedList<>();

        // Shouldn't be listed
        Vuelo v = new Vuelo(av, al);
        v.setDespegue(LocalDateTime.now().plusMinutes(1));
        vueloRepository.save(v);

        expected.add(vueloRepository.save(new Vuelo(av, al)));

        // Should be listed
        v = new Vuelo(av, al);
        v.setDespegue(LocalDateTime.now().minusMinutes(1));
        expected.add(vueloRepository.save(v));

        List<Long> expectedIds = expected.stream()
                .map(Vuelo::getId)
                .collect(Collectors.toList());

        List<Vuelo> results = vueloRepository.findDespegadosByAerolinea(al.getName());
        assertEquals(1, results.size());
        results.stream()
                .map(Vuelo::getId)
                .forEach(id -> assertTrue(expectedIds.contains(id)));
    }
}
