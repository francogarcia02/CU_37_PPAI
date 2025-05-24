package entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CambioEstado {
    private Long idCambioEstado;
    private Estado estadoAnterior;
    private Estado estadoNuevo;
    private LocalDateTime fechaHorainicio;
    private LocalDateTime fechaHorafin;
    private Empleado responsableCambioEstado;
    private List<MotivoFueraServicio> motivosCambioEstados;

    public CambioEstado(Long idCambioEstado, Estado estadoAnterior, Estado estadoNuevo, LocalDateTime fechaHorainicio, LocalDateTime fechaHorafin, Empleado responsableCambioEstado, List<MotivoFueraServicio> motivosCambioEstados) {
        this.idCambioEstado = idCambioEstado;
        this.estadoAnterior = estadoAnterior;
        this.estadoNuevo = estadoNuevo;
        this.fechaHorainicio = fechaHorainicio;
        this.fechaHorafin = fechaHorafin;
        this.responsableCambioEstado = responsableCambioEstado;
        this.motivosCambioEstados = motivosCambioEstados;
    }
}
