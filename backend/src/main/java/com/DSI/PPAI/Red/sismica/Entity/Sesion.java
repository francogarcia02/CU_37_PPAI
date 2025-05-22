package com.DSI.PPAI.Red.sismica.Entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Optional;

@Data
public class Sesion {
    private Usuario usuario;
    private Estado estado; // ACTIVA, INACTIVA
    private LocalDateTime fechaInicio;
    private Optional<LocalDateTime> fechaFin;

}
