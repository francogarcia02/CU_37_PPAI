package com.DSI.PPAI.Red.sismica.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;


import java.time.LocalDateTime;

@Data
public class CambioEstado {


    private Number  id;
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;


    private Estado estado;
    private String[] motivosFueraDeServicio;
    private Empleado responsableInspeccion;

    public CambioEstado(LocalDateTime fechaHoraInicio, Estado estado, String[] motivosFueraDeServicio, Empleado responsableInspeccion) {
        this.fechaHoraInicio = fechaHoraInicio;
        this.estado = estado;
        this.motivosFueraDeServicio = motivosFueraDeServicio;
        this.responsableInspeccion = responsableInspeccion;
    }
}
