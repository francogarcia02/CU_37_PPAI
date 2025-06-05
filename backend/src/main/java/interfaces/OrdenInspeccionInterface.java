package interfaces;

import entity.CambioEstado;
import entity.Empleado;
import entity.Estado;
import entity.MotivoFueraServicio;

import java.time.LocalDateTime;
import java.util.List;

public interface OrdenInspeccionInterface {
    public Boolean esTuRI(Empleado empleado);
    public Boolean estaRealizada();
    public Boolean cerrar(String observacion, List<MotivoFueraServicio> motivosNuevos, Estado estadoCerrada, Empleado responsableEjecucion);
    public void realizar();
    public void confirmarPte();
    public void finalizar();
    public String toStringForPantalla();
    public void enviarSismografoAReparar(Estado estadoFs);
    public LocalDateTime obtenerFechaFinalizacion();
    public CambioEstado obtenerCambioEstadoActual();
    public Boolean compareNroOrder(Long number);

}
