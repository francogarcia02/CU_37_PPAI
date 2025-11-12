package entity;

import lombok.Data;

import java.util.List;

@Data
public class TipoMotivo {
    public int idTipoMotivo;
    public String descripcion;

    public TipoMotivo(int idTipoMotivo, String descripcion) {
        this.idTipoMotivo = idTipoMotivo;
        this.descripcion = descripcion;
    }
}
