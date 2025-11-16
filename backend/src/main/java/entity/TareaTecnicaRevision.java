package entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "T_TAREA_TECNICA_REVISION")
public class TareaTecnicaRevision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tarea")
    private Long codigo;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "descripcion_trabajo")
    private String descripcionTrabajo;

    @Column(name = "duracion_estimada")
    private String duracionEstimada;

    @Column(name = "comentario")
    private String comentario;

    @Enumerated(EnumType.STRING)
    @Column(name = "apreciacion")
    private Apreciacion apreciacion;

    @Column(name = "resultado_inspeccion")
    private String resultadoInspeccion;

    @Column(name = "registro_realizacion")
    private Boolean registroRealizacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_orden")
    private OrdenInspeccion ordenInspeccion;

}
