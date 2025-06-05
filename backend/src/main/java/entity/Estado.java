package entity;

import lombok.Data;

@Data
public class Estado {
    public String ambito;
    public String nombre;

    public Estado(String ambito, String nombre) {
        this.ambito = ambito;
        this.nombre = nombre;
    }
}
