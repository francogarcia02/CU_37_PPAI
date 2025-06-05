package entity;

import lombok.Data;

@Data
public class Sismografo {
    public Long idSismografo;
    public String fabricante;
    public String modelo;
    public Estado estado;

    public Sismografo(Long idSismografo, String fabricante, String modelo, Estado estado) {
        this.idSismografo = idSismografo;
        this.fabricante = fabricante;
        this.modelo = modelo;
        this.estado = estado;
    }

    public void enviarSismografoAReparar(Estado estadoFS) {
        this.estado = estadoFS;
    }
}
