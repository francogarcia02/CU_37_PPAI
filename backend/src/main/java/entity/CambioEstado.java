package entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CambioEstado {
    public Long idCambioEstado;
    public Estado estadoAnterior;
    public Estado estadoNuevo;
    public LocalDateTime fechaHorainicio;
    public LocalDateTime fechaHorafin;
    public Empleado responsableCambioEstado;
    public List<MotivoFueraServicio> motivosCambioEstados;

    public CambioEstado(Long idCambioEstado, Estado estadoAnterior, Estado estadoNuevo, LocalDateTime fechaHorainicio, LocalDateTime fechaHorafin, Empleado responsableCambioEstado, List<MotivoFueraServicio> motivosCambioEstados) {
        this.idCambioEstado = idCambioEstado;
        this.estadoAnterior = estadoAnterior;
        this.estadoNuevo = estadoNuevo;
        this.fechaHorainicio = fechaHorainicio;
        this.fechaHorafin = fechaHorafin;
        this.responsableCambioEstado = responsableCambioEstado;
        this.motivosCambioEstados = motivosCambioEstados;
    }

    public Boolean esFinalizado() {
        Boolean result = "finalizada".equals(this.getEstadoNuevo().getNombre());
        return result;
    }
}
