package entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "T_CAMBIO_ESTADO")
public class CambioEstado {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cambio_estado_seq")
    @SequenceGenerator(
            name = "cambio_estado_seq",
            sequenceName = "cambio_estado_seq",
            allocationSize = 1,
            initialValue = 6 // Un número mayor que el ID más alto en tus datos iniciales
    )
    @Column(name = "id_cambio_estado")
    private Long idCambioEstado;

    @ManyToOne
    @JoinColumn(name = "id_estado_anterior")
    private Estado estadoAnterior;

    @ManyToOne
    @JoinColumn(name = "id_estado_nuevo", nullable = false)
    private Estado estadoNuevo;

    @Column(name = "fecha_hora_inicio", nullable = false)
    private LocalDateTime fechaHorainicio;

    @Column(name = "fecha_hora_fin")
    private LocalDateTime fechaHorafin;

    @ManyToOne
    @JoinColumn(name = "id_responsable_cambio")
    private Empleado responsableCambioEstado;

    @OneToMany(mappedBy = "cambioEstado", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<MotivoFueraServicio> motivosCambioEstados = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_orden")
    @ToString.Exclude
    private OrdenInspeccion ordenInspeccion;


    public CambioEstado(Long idCambioEstado, Estado estadoAnterior, Estado estadoNuevo, LocalDateTime fechaHorainicio, LocalDateTime fechaHorafin, Empleado responsableCambioEstado, List<MotivoFueraServicio> motivosCambioEstados) {
        this.idCambioEstado = idCambioEstado;
        this.estadoAnterior = estadoAnterior;
        this.estadoNuevo = estadoNuevo;
        this.fechaHorainicio = fechaHorainicio;
        this.fechaHorafin = fechaHorafin;
        this.responsableCambioEstado = responsableCambioEstado;
        this.setMotivosCambioEstados(motivosCambioEstados); // Usar el nuevo método
    }

    public Boolean esFinalizado() {
        return estadoNuevo.esFinalizado();
    }

    // Método de conveniencia para sincronizar la relación bidireccional
    public void setMotivosCambioEstados(List<MotivoFueraServicio> motivos) {
        this.motivosCambioEstados.clear();
        if (motivos != null) {
            for (MotivoFueraServicio motivo : motivos) {
                motivo.setCambioEstado(this); // Establecer la referencia inversa
                this.motivosCambioEstados.add(motivo);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CambioEstado that = (CambioEstado) o;
        return Objects.equals(idCambioEstado, that.idCambioEstado);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCambioEstado);
    }
}
