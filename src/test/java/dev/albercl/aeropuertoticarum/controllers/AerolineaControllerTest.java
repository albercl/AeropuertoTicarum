package dev.albercl.aeropuertoticarum.controllers;

import dev.albercl.aeropuertoticarum.AeropuertoTicarum;
import dev.albercl.aeropuertoticarum.model.Aerolinea;
import dev.albercl.aeropuertoticarum.repositories.AerolineaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

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
class AerolineaControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private AerolineaRepository repository;

    private Aerolinea generateTestAerolinea(String name) {
        Aerolinea a = new Aerolinea();
        a.setName(name);

        return repository.save(a);
    }

    @Test
    void getAerolineaInfoShouldReturnInfo() throws Exception {
        String alName = "test al";
        Aerolinea test = generateTestAerolinea(alName);

        mvc.perform(get("/{aerolineaName}/services/info", test.getName()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(alName)));
    }

    @Test
    void getAerolineaInfoShouldReturnNotFound() throws Exception {
        mvc.perform(get("/nonexistingal/services/info"))
                .andExpect(status().isNotFound());
    }
}