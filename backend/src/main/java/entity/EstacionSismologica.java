package entity;

import lombok.Data;

import java.util.List;

@Data
public class EstacionSismologica {
    private Number codigoEstacion;
    private String nombreEstacion;
    private Number latitud;
    private Number longitud;
    private String fechaSolicitudCertificacion;
    private Number nroCertificacionAdquisicion;
    private String documentoCertificacionAdq;
    private Sismografo sismografo;

    public EstacionSismologica(Number codigoEstacion, String nombreEstacion, Number latitud, Number longitud, Number nroCertAdq, String fechaSolCer, String docCertAdq, Sismografo sismografo) {
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
