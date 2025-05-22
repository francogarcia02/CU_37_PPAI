package com.DSI.PPAI.Red.sismica.Entity;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class OrdenInspeccion {

    private Long numeroOrden;
    private LocalDate fechaFinalizacion;
    private String observacionCierre;
    private Estado estado;
    private EstacionSismologica estacion;
    private Empleado responsable;
    private LocalDateTime fechaCierre;



}

