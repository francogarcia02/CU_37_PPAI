package entity;

import lombok.Data;

@Data
public class Sismografo {
    private Long idSismografo;
    private String fabricante;
    private String modelo;
    private Estado estado;

    public Sismografo(Long idSismografo, String fabricante, String modelo, Estado estado) {
        this.idSismografo = idSismografo;
        this.fabricante = fabricante;
        this.modelo = modelo;
        this.estado = estado;
    }

    public void ponerEnFueraServicio(Estado estadoFS) {
        this.estado = estadoFS;
    }
}
