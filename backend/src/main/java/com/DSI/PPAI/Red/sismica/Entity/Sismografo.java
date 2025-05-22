package com.DSI.PPAI.Red.sismica.Entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Data
public class Sismografo {
    private Long id;
    private LocalDateTime fechaAdquisicion;
    private Long numeroDeSerie;
    private List<Reparacion> reparaciones;
    private Estado estado;
    private List<CambioEstado> cambiosEstados;
}
