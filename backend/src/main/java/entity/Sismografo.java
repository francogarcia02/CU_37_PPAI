package entity;

import lombok.Data;

@Data
public class Sismografo {
    public Long idSismografo;
    public String fechaAdquisicion;
    public Number numeroDeSerie;
    public String fabricante;
    public String modelo;
    public Estado estadoActual;

    public Sismografo(Long idSismografo, String fechaAdq, Number nroDeSerie, String fabricante, String modelo, Estado estado) {
        this.idSismografo = idSismografo;
        this.fechaAdquisicion = fechaAdq;
        this.numeroDeSerie = nroDeSerie;
        this.fabricante = fabricante;
        this.modelo = modelo;
        this.estadoActual = estado;
    }

    public void enviarSismografoAReparar(Estado estadoFS) {
        this.estadoActual = estadoFS;
    }
}
