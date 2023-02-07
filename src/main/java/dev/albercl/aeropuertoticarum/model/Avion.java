package dev.albercl.aeropuertoticarum.model;

import javax.persistence.*;

@Entity
@Table(name = "Avion")
public class Avion {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private Integer capacity;

    @ManyToOne
    @JoinColumn(name = "aerolinea_id", nullable = false)
    private Aerolinea aerolinea;

    public Avion() {
    }

    public Avion(String model, Integer capacity, Aerolinea aerolinea) {
        this.model = model;
        this.capacity = capacity;
        this.aerolinea = aerolinea;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Aerolinea getAerolinea() {
        return aerolinea;
    }

    public void setAerolinea(Aerolinea aerolinea) {
        this.aerolinea = aerolinea;
    }
}
