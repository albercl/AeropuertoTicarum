package dev.albercl.aeropuertoticarum.controllers;

import dev.albercl.aeropuertoticarum.AeropuertoTicarum;
import dev.albercl.aeropuertoticarum.model.Aerolinea;
import dev.albercl.aeropuertoticarum.model.Avion;
import dev.albercl.aeropuertoticarum.model.Vuelo;
import dev.albercl.aeropuertoticarum.repositories.AerolineaRepository;
import dev.albercl.aeropuertoticarum.repositories.AvionRepository;
import dev.albercl.aeropuertoticarum.repositories.VueloRepository;
import dev.albercl.aeropuertoticarum.services.AerolineaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = AeropuertoTicarum.class)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(
        locations = "classpath:application-test.properties"
)
public class SalidaControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private AerolineaService services;

    @Autowired
    private AerolineaRepository aerolineaRepository;
    @Autowired
    private VueloRepository vueloRepository;
    @Autowired
    private AvionRepository avionRepository;

    private Aerolinea aerolinea;
    private Avion avion;
    private Vuelo vuelo;

    @BeforeEach
    void createTestData() {
        vueloRepository.deleteAll();
        avionRepository.deleteAll();
        aerolineaRepository.deleteAll();

        aerolinea = new Aerolinea();
        aerolinea.setName("AerolineaTest");
        aerolinea = aerolineaRepository.save(aerolinea);

        avion = new Avion();
        avion.setCapacidad(200);
        avion.setModelo("Test model");
        avion.setAerolinea(aerolinea);
        avion = avionRepository.save(avion);

        vuelo = new Vuelo();
        vuelo.setAvion(avion);
        vuelo.setAerolinea(aerolinea);
        vuelo = vueloRepository.save(vuelo);
    }

    @Test
    void getSalida_NoSalidas_EmptyList() throws Exception {
        mvc.perform(get("/" + aerolinea.getName() + "/services/salida"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void getSalida_VuelosSinSalidas_EmptyList() throws Exception {
        services.addVuelo(aerolinea.getName(), new Vuelo(avion));
        Vuelo v = new Vuelo(avion, aerolinea);
        v.setDespegue(LocalDateTime.now().plusMinutes(1));
        vueloRepository.save(v);

        mvc.perform(get("/" + aerolinea.getName() + "/services/salida"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void getSalida_MultipleSalidas_ListOfSalidas() throws Exception {
        services.addVuelo(aerolinea.getName(), new Vuelo(avion));
        Vuelo v = new Vuelo(avion, aerolinea);
        v.setDespegue(LocalDateTime.now().minusMinutes(1));
        vueloRepository.save(v);

        mvc.perform(get("/" + aerolinea.getName() + "/services/salida"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(v.getId().intValue())));
    }

    @Test
    void getSalidaVuelo_VueloNoExiste_NotFound() throws Exception {
        mvc.perform(get("/" + aerolinea.getName() + "/services/salida/-1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getSalidaVuelo_VueloNoTieneSalida_SalidaFalse() throws Exception {
        mvc.perform(get("/" + aerolinea.getName() + "/services/salida/" + vuelo.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.haDespegado", is(false)))
                .andExpect(jsonPath("$.id", is(vuelo.getId().intValue())))
                .andExpect(jsonPath("$.horaDespegue", nullValue()));
    }

    @Test
    void getSalidaVuelo_VueloTieneSalida_SalidaTrue() throws Exception {
        services.despegarVuelo(vuelo.getId());

        mvc.perform(get("/" + aerolinea.getName() + "/services/salida/" + vuelo.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.haDespegado", is(true)))
                .andExpect(jsonPath("$.id", is(vuelo.getId().intValue())))
                .andExpect(jsonPath("$.horaDespegue", notNullValue()));
    }

    @Test
    void postDespegueVuelo_VueloNoExiste_NotFound() throws Exception {
        mvc.perform(post("/" + aerolinea.getName() + "/services/salida/-1/despegue"))
                .andExpect(status().isNotFound());
    }

    @Test
    void postDespegueVuelo_VueloNoTieneSalida_NoContent() throws Exception {
        mvc.perform(post("/" + aerolinea.getName() + "/services/salida/" + vuelo.getId() + "/despegue"))
                .andExpect(status().isNoContent());

        Vuelo vueloDespegado = vueloRepository.findById(vuelo.getId()).orElse(null);
        assertNotNull(vueloDespegado);
        assertNotNull(vueloDespegado.getDespegue());
    }

    @Test
    void postDespegueVuelo_VueloTieneSalida_BadRequest() throws Exception {
        services.despegarVuelo(vuelo.getId());

        mvc.perform(post("/" + aerolinea.getName() + "/services/salida/" + vuelo.getId() + "/despegue"))
                .andExpect(status().isBadRequest());
    }
}
