package com.DSI.PPAI.Red.sismica.Entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EstacionSismologica {
    private Long codigoEstacion;
    private String documentoCertificacionAdq;
    private LocalDateTime fechaSolicitudCertificacion;
    private float latitud;
    private float longitud;
    private String nombre;
    private Long numeroCertificacionAdquisicion;
    private Sismografo sismografo;
}
