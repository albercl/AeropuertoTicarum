package dev.albercl.aeropuertoticarum.dto;

import java.time.LocalDateTime;

public class VueloDto {
    private long id;
    private long avion;
    private long aerolinea;
    private LocalDateTime despegue;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAvion() {
        return avion;
    }

    public void setAvion(long avion) {
        this.avion = avion;
    }

    public long getAerolinea() {
        return aerolinea;
    }

    public void setAerolinea(long aerolinea) {
        this.aerolinea = aerolinea;
    }

    public LocalDateTime getDespegue() {
        return despegue;
    }

    public void setDespegue(LocalDateTime despegue) {
        this.despegue = despegue;
    }
}
