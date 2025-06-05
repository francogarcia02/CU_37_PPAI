package entity;

import lombok.Data;

import java.util.List;

@Data
public class TipoMotivo {
    public String descripcion;

    public TipoMotivo(String descripcion) {
        this.descripcion = descripcion;
    }
}
