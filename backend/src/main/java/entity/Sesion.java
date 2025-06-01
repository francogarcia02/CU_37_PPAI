package entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Sesion {
    private Usuario usuario;
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;
    private Boolean isActiva;

    public Sesion(Usuario usuario) {
        this.usuario = usuario;
        this.fechaHoraInicio = LocalDateTime.now();
        this.fechaHoraFin = null;
        this.isActiva = true;
    }
}
