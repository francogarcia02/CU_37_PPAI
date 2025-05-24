package entity;

import lombok.Data;

import java.util.List;

@Data
public class MotivoFueraServicio {
    private String comentario;
    private TipoMotivo tipoMotivo;

    public MotivoFueraServicio(String comentario, TipoMotivo tipoMotivo) {
        this.comentario = comentario;
        this.tipoMotivo = tipoMotivo;
    }
}
