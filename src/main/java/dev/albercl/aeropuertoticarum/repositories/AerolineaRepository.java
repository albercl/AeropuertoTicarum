package dev.albercl.aeropuertoticarum.repositories;

import dev.albercl.aeropuertoticarum.model.Aerolinea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AerolineaRepository extends JpaRepository<Aerolinea, Long> {
    Optional<Aerolinea> findByName(@Param("name") String name);
}
