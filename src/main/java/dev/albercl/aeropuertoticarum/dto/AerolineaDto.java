package dev.albercl.aeropuertoticarum.dto;

public class AerolineaDto {

    private Long id;
    private String name;
    private Long numeroAviones;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getNumeroAviones() {
        return numeroAviones;
    }

    public void setNumeroAviones(Long numeroAviones) {
        this.numeroAviones = numeroAviones;
    }
}
