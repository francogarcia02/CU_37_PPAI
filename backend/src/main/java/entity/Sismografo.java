package entity;

import lombok.Data;

@Data
public class Sismografo {
    private Long idSismografo;
    private String fabricante;
    private String modelo;

    public Sismografo(Long idSismografo, String fabricante, String modelo) {
        this.idSismografo = idSismografo;
        this.fabricante = fabricante;
        this.modelo = modelo;
    }
}
