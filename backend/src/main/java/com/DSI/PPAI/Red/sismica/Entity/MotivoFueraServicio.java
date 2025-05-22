package com.DSI.PPAI.Red.sismica.Entity;

import lombok.Data;

@Data
public class MotivoFueraServicio {
    private Long id;
    private Empleado autor;
    private String tipoMotivo;
    private String comentario;
}
