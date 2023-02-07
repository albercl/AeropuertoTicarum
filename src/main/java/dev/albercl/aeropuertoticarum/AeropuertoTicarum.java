package dev.albercl.aeropuertoticarum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AeropuertoTicarum {
    public static void main(String[] args) {
        SpringApplication.run(AeropuertoTicarum.class, args);
    }
}