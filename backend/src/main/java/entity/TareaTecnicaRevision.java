package entity;

import lombok.Data;

@Data
public class TareaTecnicaRevision {
    private Long codigo;
    private String nombre;
    private String descripcionTrabajo;
    private String duracionEstimada;
    private String comentrario;
    private Apreciacion apreciacion;
    private String resultadoInspeccion;
    private boolean registroRealizacion;

}
