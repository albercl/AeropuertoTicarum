package dev.albercl.aeropuertoticarum.model;

import org.hibernate.annotations.Formula;

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

    @Formula("(select count(*) from Avion av where av.aerolinea_id = id)")
    private Long numeroAviones;

    public Aerolinea() {
    }

    public Aerolinea(String name, Long numeroAviones) {
        this.name = name;
        this.numeroAviones = numeroAviones;
    }

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
}
