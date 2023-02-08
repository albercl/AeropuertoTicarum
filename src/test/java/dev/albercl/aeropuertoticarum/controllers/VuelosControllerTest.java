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
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @Autowired
    private AerolineaService service;

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
    void getVuelosPendientes_VueloPendiente_ListOfVuelos() throws Exception {
        mvc.perform(get("/" + aerolinea.getName() + "/services/vuelo")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(vuelo.getId().intValue())));
    }

    @Test
    void getVuelosPendientes_VueloDespegado_EmptyList() throws Exception {
        service.despegarVuelo(vuelo.getId());

        mvc.perform(get("/" + aerolinea.getName() + "/services/vuelo")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void postNuevoVuelo_ValidVuelo_VueloCreated() throws Exception {
        // Create a new vuelo with avion and aerolinea in json
        String json = String.format("{\"avion\": %d}", avion.getId());

        mvc.perform(post("/" + aerolinea.getName() + "/services/vuelo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.avion", is(avion.getId().intValue())))
                .andExpect(jsonPath("$.aerolinea", is(aerolinea.getId().intValue())))
                .andExpect(jsonPath("$.despegue", nullValue()))
                .andExpect(jsonPath("$.entrada", notNullValue()));
    }

    @Test
    void postNuevoVuelo_InvalidAerolinea_NotFound() throws Exception {
        // Create a new vuelo with avion
        String json = String.format("{\"avion\": %d}", avion.getId());

        mvc.perform(post("/aerolineafalsa/services/vuelo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    void postNuevoVuelo_EmptyVuelo_BadRequest() throws Exception {
        // Create a new vuelo without avion
        String json = "{}";

        mvc.perform(post("/" + aerolinea.getName() + "/services/vuelo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getVuelo_ValidVuelo_Vuelo() throws Exception {
        Vuelo v = service.addVuelo(aerolinea.getName(), new Vuelo(avion));

        mvc.perform(get("/" + aerolinea.getName() + "/services/vuelo/" + v.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(v.getId().intValue())))
                .andExpect(jsonPath("$.avion", is(avion.getId().intValue())))
                .andExpect(jsonPath("$.aerolinea", is(aerolinea.getId().intValue())))
                .andExpect(jsonPath("$.entrada", notNullValue()));
    }

    @Test
    void getVuelo_InvalidVuelo_NotFound() throws Exception {
        mvc.perform(get("/" + aerolinea.getName() + "/services/vuelo/-1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void patchVuelo_InvalidVuelo_NotFound() throws Exception {
        mvc.perform(patch("/" + aerolinea.getName() + "/services/vuelo/-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void patchVuelo_InvalidAvionId_BadRequest() throws Exception {
        Vuelo v = service.addVuelo(aerolinea.getName(), new Vuelo(avion));
        String json = String.format("{\"avion\": %d}", -1);

        mvc.perform(patch("/" + aerolinea.getName() + "/services/vuelo/" + v.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void patchVuelo_WithoutAvion_BadRequest() throws Exception {
        Vuelo v = service.addVuelo(aerolinea.getName(), new Vuelo(avion));
        String json = "{}";

        mvc.perform(patch("/" + aerolinea.getName() + "/services/vuelo/" + v.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void patchVuelo_ValidVuelo_Vuelo() throws Exception {
        Vuelo v = service.addVuelo(aerolinea.getName(), new Vuelo(avion));
        String json = String.format("{\"avion\": %d}", avion.getId());

        mvc.perform(patch("/" + aerolinea.getName() + "/services/vuelo/" + v.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(v.getId().intValue())))
                .andExpect(jsonPath("$.avion", is(avion.getId().intValue())))
                .andExpect(jsonPath("$.aerolinea", is(aerolinea.getId().intValue())))
                .andExpect(jsonPath("$.entrada", notNullValue()));
    }

    @Test
    void deleteVuelo_InvalidVuelo_NotFound() throws Exception {
        mvc.perform(delete("/" + aerolinea.getName() + "/services/vuelo/-1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteVuelo_ValidVuelo_Vuelo() throws Exception {
        Vuelo v = service.addVuelo(aerolinea.getName(), new Vuelo(avion));

        mvc.perform(delete("/" + aerolinea.getName() + "/services/vuelo/" + v.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
