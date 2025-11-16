package entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "T_MOTIVO_FS")
public class MotivoFueraServicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_motivo_fs")
    private Long idMotivoFs;

    @Column(name = "comentario")
    private String comentario;

    @ManyToOne
    @JoinColumn(name = "id_tipo_motivo", nullable = false)
    private TipoMotivo tipoMotivo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cambio_estado")
    @ToString.Exclude
    private CambioEstado cambioEstado;

    public MotivoFueraServicio(String comentario, TipoMotivo tipoMotivo) {
        this.comentario = comentario;
        this.tipoMotivo = tipoMotivo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MotivoFueraServicio that = (MotivoFueraServicio) o;
        return Objects.equals(idMotivoFs, that.idMotivoFs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idMotivoFs);
    }
}
