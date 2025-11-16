package entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "T_ESTACION_SISMOLOGICA")
public class EstacionSismologica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estacion")
    private Long codigoEstacion;

    @Column(name = "nombre_estacion", nullable = false)
    private String nombreEstacion;

    @Column(name = "latitud")
    private Double latitud;

    @Column(name = "longitud")
    private Double longitud;

    @Column(name = "fecha_solicitud_certificacion")
    private String fechaSolicitudCertificacion;

    @Column(name = "nro_certificacion_adquisicion")
    private Long nroCertificacionAdquisicion;

    @Column(name = "documento_certificacion_adq")
    private String documentoCertificacionAdq;

    @OneToOne
    @JoinColumn(name = "id_sismografo")
    private Sismografo sismografo;

    public EstacionSismologica(Long codigoEstacion, String nombreEstacion, Double latitud, Double longitud, Long nroCertAdq, String fechaSolCer, String docCertAdq, Sismografo sismografo) {
        this.codigoEstacion = codigoEstacion;
        this.nombreEstacion = nombreEstacion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.nroCertificacionAdquisicion = nroCertAdq;
        this.fechaSolicitudCertificacion = fechaSolCer;
        this.documentoCertificacionAdq = docCertAdq;
        this.sismografo = sismografo;
    }

    public void enviarSismografoAReparar(Estado estadoFs){
        Sismografo sismografoSelected = this.getSismografo();
        sismografoSelected.enviarSismografoAReparar(estadoFs);
    }
}
