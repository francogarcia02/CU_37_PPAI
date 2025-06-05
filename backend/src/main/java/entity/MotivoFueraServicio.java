package entity;

import lombok.Data;

import java.util.List;

@Data
public class MotivoFueraServicio {
    public String comentario;
    public TipoMotivo tipoMotivo;

    public MotivoFueraServicio(String comentario, TipoMotivo tipoMotivo) {
        this.comentario = comentario;
        this.tipoMotivo = tipoMotivo;
    }
}
