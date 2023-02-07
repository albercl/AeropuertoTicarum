package dev.albercl.aeropuertoticarum.model;

import javax.persistence.*;

@Entity
@Table(name = "Aerolinea")
public class Aerolinea {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(nullable = false)
    private Long numeroAviones = 0l;

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
