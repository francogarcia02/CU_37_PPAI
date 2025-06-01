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

    public void enviarSismografoAReparar(Estado estadoFs){
        Sismografo sismografoSelected = this.getSismografo();
        sismografoSelected.enviarSismografoAReparar(estadoFs);
    }
}
