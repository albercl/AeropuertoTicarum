package dev.albercl.aeropuertoticarum.dto;

import java.time.LocalDateTime;

public class SalidaDto {

    private Long id;
    private boolean haDespegado;
    private LocalDateTime horaDespegue;

    public SalidaDto() {
    }

    public SalidaDto(Long id, boolean haDespegado, LocalDateTime horaDespegue) {
        this.id = id;
        this.haDespegado = haDespegado;
        this.horaDespegue = horaDespegue;
    }

    public boolean isHaDespegado() {
        return haDespegado;
    }

    public void setHaDespegado(boolean haDespegado) {
        this.haDespegado = haDespegado;
    }

    public LocalDateTime getHoraDespegue() {
        return horaDespegue;
    }

    public void setHoraDespegue(LocalDateTime horaDespegue) {
        this.horaDespegue = horaDespegue;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
