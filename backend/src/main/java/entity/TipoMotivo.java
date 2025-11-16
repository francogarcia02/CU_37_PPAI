package entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "T_TIPO_MOTIVO")
public class TipoMotivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_motivo")
    private int idTipoMotivo;

    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    public TipoMotivo(int idTipoMotivo, String descripcion) {
        this.idTipoMotivo = idTipoMotivo;
        this.descripcion = descripcion;
    }
}
