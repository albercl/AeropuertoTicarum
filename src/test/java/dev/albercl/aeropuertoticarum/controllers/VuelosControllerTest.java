package dev.albercl.aeropuertoticarum.controllers;

import com.jayway.jsonpath.JsonPath;
import dev.albercl.aeropuertoticarum.AeropuertoTicarum;
import dev.albercl.aeropuertoticarum.model.Aerolinea;
import dev.albercl.aeropuertoticarum.model.Avion;
import dev.albercl.aeropuertoticarum.model.Vuelo;
import dev.albercl.aeropuertoticarum.repositories.AerolineaRepository;
import dev.albercl.aeropuertoticarum.repositories.AvionRepository;
import dev.albercl.aeropuertoticarum.repositories.VueloRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = AeropuertoTicarum.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-test.properties"
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class VuelosControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private AerolineaRepository aerolineaRepository;

    @Autowired
    private VueloRepository vueloRepository;

    @Autowired
    private AvionRepository avionRepository;

    private Aerolinea aerolinea;
    private Avion avion;

    private Vuelo vuelo;

    @BeforeAll
    void createTestData() {
        Aerolinea a = new Aerolinea();
        a.setName("Aerolinea Test");

        aerolinea = aerolineaRepository.save(a);

        Avion av = new Avion();
        av.setCapacity(200);
        av.setModel("Test model");

        avion = avionRepository.save(av);

        Vuelo v = new Vuelo();
        v.setAvion(avion);
        v.setAerolinea(aerolinea);

        vuelo = vueloRepository.save(v);
    }

    @Test
    void getVueloShouldReturnVuelo() throws Exception {
        mvc.perform(get("/{aerolinea}/services/vuelo", aerolinea.getName()))
                .andExpectAll(
                        jsonPath("$", hasSize(1)),
                        jsonPath("$.[0].avion", is(avion.getId().intValue())),
                        jsonPath("$.[0].aerolinea", is(aerolinea.getId().intValue()))
                );
    }

    @Test
    void getVueloShouldntReturnVueloDespegado() throws Exception {
        vuelo.setDespegue(LocalDateTime.now());

        vueloRepository.save(vuelo);

        mvc.perform(get("/{aerolinea}/services/vuelo", aerolinea.getName()))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void postVueloShouldSaveVuelo() throws Exception {
        MvcResult result = mvc.perform(post("/{aerolinea}/services/vuelo", aerolinea.getName()))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id", notNullValue()),
                        jsonPath("$.avion", is(avion.getId())),
                        jsonPath("$.aerolinea", is(avion.getId())),
                        jsonPath("$.despegue", blankOrNullString()),
                        jsonPath("$.salida", blankOrNullString())
                )
                .andReturn();

        long vueloId = JsonPath.read(result.getResponse().getContentAsString(), "$.id");
        Assertions.assertTrue(vueloRepository.findById(vueloId).isPresent());
    }

    @Test
    void postVueloWithoutAvionShouldReturnBadRequest() throws Exception {
        String vueloJson = "{}";

        mvc.perform(post("/{aerolinea}/services/vuelo", aerolinea.getName())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(vueloJson))
                .andExpectAll(status().isBadRequest())
                .andReturn();
    }

    @Test
    void postVueloWithInvalidAvionShouldReturnBadRequest() throws Exception {
        String vuelo = "{avion: null}";

        mvc.perform(post("/{aerolinea}/services/vuelo", aerolinea.getName())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(vuelo))
                .andExpectAll(status().isBadRequest())
                .andReturn();
    }

    @Test
    void getVueloByIdShouldReturnNotFound() throws Exception {
        mvc.perform(get("/{aerolinea}/services/vuelo/{id}", aerolinea.getName(), -1))
                .andExpect(status().isNotFound());
    }

    @Test
    void getVueloByIdShouldReturnVuelo() throws Exception {
        mvc.perform(get("/{aerolinea}/services/vuelo/{id}", aerolinea.getName(), vuelo.getId()))
                .andExpectAll(
                        jsonPath("$.despegue", is(vuelo.getDespegue().toString())),
                        jsonPath("$.aerolinea", is(aerolinea.getId())),
                        jsonPath("$.avion", is(avion.getId())),
                        jsonPath("$.id", notNullValue())
                );
    }

    @Test
    void putVueloShouldReturnNotFound() throws Exception {
        mvc.perform(put("/{aerolinea}/services/vuelo/{id}", aerolinea.getName(), -1))
                .andExpect(status().isNotFound());
    }

    @Test
    void putVueloShouldReturnNoContent() throws Exception {
        vuelo.setDespegue(LocalDateTime.now().plus(2, ChronoUnit.HOURS));
        vueloRepository.save(vuelo);

        String content = String.format(
                "{avion: %d, aerolinea: %d, despegue: %s}",
                vuelo.getAvion().getId(),
                vuelo.getAerolinea().getId(),
                vuelo.getDespegue().toString()
        );

        MvcResult result = mvc.perform(
                put("/{aerolinea}/services/vuelo/{id}", aerolinea.getName(), vuelo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isNoContent())
                .andReturn();

        Vuelo updatedVuelo = vueloRepository.findById(vuelo.getId()).orElse(null);

        Assertions.assertEquals(updatedVuelo.getDespegue(), vuelo.getDespegue());
    }
}
