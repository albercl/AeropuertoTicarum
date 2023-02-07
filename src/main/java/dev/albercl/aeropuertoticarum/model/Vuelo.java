package dev.albercl.aeropuertoticarum.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Vuelo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "avion_id")
    private Avion avion;

    @ManyToOne(optional = false)
    @JoinColumn(name = "aerolinea_id")
    private Aerolinea aerolinea;

    private LocalDateTime entrada;

    private LocalDateTime despegue;

    public Vuelo() {
        entrada = LocalDateTime.now();
    }

    public Vuelo(Avion avion) {
        this();
        this.avion = avion;
    }

    public Vuelo(Avion avion, Aerolinea aerolinea) {
        this(avion);
        this.aerolinea = aerolinea;
    }

    public Aerolinea getAerolinea() {
        return aerolinea;
    }

    public void setAerolinea(Aerolinea aerolinea) {
        this.aerolinea = aerolinea;
    }

    public Avion getAvion() {
        return avion;
    }

    public void setAvion(Avion avion) {
        this.avion = avion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDespegue() {
        return despegue;
    }

    public void setDespegue(LocalDateTime despegue) {
        this.despegue = despegue;
    }

    public LocalDateTime getEntrada() {
        return entrada;
    }

    public void setEntrada(LocalDateTime entrada) {
        this.entrada = entrada;
    }
}
