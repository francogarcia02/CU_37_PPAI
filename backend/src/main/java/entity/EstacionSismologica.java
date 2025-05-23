package entity;

import lombok.Data;

import java.util.List;

@Data
public class EstacionSismologica {
    private String nombreEstacion;
    private Sismografo sismografo;

    public EstacionSismologica(String nombreEstacion, Sismografo sismografo) {
        this.nombreEstacion = nombreEstacion;
        this.sismografo = sismografo;

    }
}
