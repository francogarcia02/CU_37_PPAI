package entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Sesion {
    private Usuario usuario;
    private LocalDateTime fechaHoraInicioSesion;
    private Boolean isActiva;

    public Sesion(Usuario usuario) {
        this.usuario = usuario;
        this.fechaHoraInicioSesion = LocalDateTime.now();
        this.isActiva = true;
    }
}
