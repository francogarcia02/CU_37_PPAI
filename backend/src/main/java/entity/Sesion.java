package entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Sesion {
    public Usuario usuario;
    public LocalDateTime fechaHoraInicio;
    public LocalDateTime fechaHoraFin;
    public Boolean isActiva;

    public Sesion(Usuario usuario) {
        this.usuario = usuario;
        this.fechaHoraInicio = LocalDateTime.now();
        this.fechaHoraFin = null;
        this.isActiva = true;
    }
}
