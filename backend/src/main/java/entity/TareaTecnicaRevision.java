package entity;

import lombok.Data;

@Data
public class TareaTecnicaRevision {
    public Long codigo;
    public String nombre;
    public String descripcionTrabajo;
    public String duracionEstimada;
    public String comentrario;
    public Apreciacion apreciacion;
    public String resultadoInspeccion;
    public boolean registroRealizacion;

}
