package dev.albercl.aeropuertoticarum.repositories;

import dev.albercl.aeropuertoticarum.model.Vuelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VueloRepository extends JpaRepository<Vuelo, Long> {
    @Query("select v from Vuelo v join v.aerolinea al where al.name = ?1 and (v.despegue >= current_timestamp or v.despegue = null)")
    List<Vuelo> findPendientesByAerolinea(String aerolineaName);

    @Query("select v from Vuelo v join v.aerolinea al where al.name = ?1 and v.despegue < current_timestamp")
    List<Vuelo> findDespegadosByAerolinea(String aerolineaName);
}
