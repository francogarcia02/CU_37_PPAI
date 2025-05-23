package entity;

import lombok.Data;

@Data
public class Estado {
    private String ambito;
    private String nombre;

    public Estado(String ambito, String nombre) {
        this.ambito = ambito;
        this.nombre = nombre;
    }
}
