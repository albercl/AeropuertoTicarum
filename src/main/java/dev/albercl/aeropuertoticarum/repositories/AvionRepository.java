package dev.albercl.aeropuertoticarum.repositories;

import dev.albercl.aeropuertoticarum.model.Avion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvionRepository extends JpaRepository<Avion, Long> {

    @Query("select a from Avion a join a.aerolinea al where al.name = ?1")
    List<Avion> getByAerolinea(String aerolineaName);
}
