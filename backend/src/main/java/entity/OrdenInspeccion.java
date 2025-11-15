package entity;

import control.notificacion.DatosNotificacionCierre;
import interfaces.OrdenInspeccionInterface;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrdenInspeccion implements OrdenInspeccionInterface {
    public Long numeroOrden;
    public EstacionSismologica estacionSismologica;
    public Empleado responsableOrdenInspeccion;
    public List<TareaTecnicaRevision> tareasTecnicasRevisiones;
    public String observaciones;
    public List<CambioEstado> cambiosEstados;


    public OrdenInspeccion(Long numeroOrden, EstacionSismologica estacionSismologica, List<TareaTecnicaRevision> tareasTecnicasRevisiones, Empleado responsableOrdenInspeccion, String observaciones, List<CambioEstado> cambiosEstados) {
        if (numeroOrden == null) {
            throw new IllegalArgumentException("Número de orden no puede ser nulo");
        }
        if (estacionSismologica == null) {
            throw new IllegalArgumentException("Estación sismológica no puede ser nula");
        }
        if (tareasTecnicasRevisiones == null) {
            throw new IllegalArgumentException("Lista de tareas técnicas no puede ser nula");
        }
        if (responsableOrdenInspeccion == null) {
            throw new IllegalArgumentException("Responsable de la orden no puede ser nulo");
        }
        
        this.numeroOrden = numeroOrden;
        this.estacionSismologica = estacionSismologica;
        this.tareasTecnicasRevisiones = new ArrayList<>(tareasTecnicasRevisiones);
        this.responsableOrdenInspeccion = responsableOrdenInspeccion;
        this.observaciones = observaciones;
        this.cambiosEstados = new ArrayList<>(cambiosEstados);
    }



    @Override
    public Boolean esTuRI(Empleado empleado) {
            return responsableOrdenInspeccion.equals(empleado);
        }

    public Boolean estaFinalizada() {
        CambioEstado estadoActual = this.obtenerCambioEstadoActual();
        return estadoActual != null && estadoActual.getEstadoNuevo().getNombre().equals("Finalizado");
}

    @Override
    public Boolean cerrar(String observacion, List<MotivoFueraServicio> motivosNuevos, Estado estadoCerrada, Empleado responsableEjecucion) {
        for (CambioEstado cambio : cambiosEstados) {
            if (cambio.getFechaHorafin() == null &&
                    "Finalizado".equalsIgnoreCase(cambio.getEstadoNuevo().getNombre())) {
                this.setObservaciones(observacion);
                cambio.setFechaHorafin(LocalDateTime.now());
                cambiosEstados.add(
                        new CambioEstado(
                                cambio.getIdCambioEstado() + 1L,
                                cambio.getEstadoNuevo(),
                                estadoCerrada,
                                LocalDateTime.now(),
                                null,
                                responsableEjecucion,
                                motivosNuevos
                        ));
                return true;
            }
        }
        return false;
    }


    @Override
    public void realizar() {

    }

    @Override
    public void confirmarPte() {

    }

    @Override
    public void finalizar() {

    }



    public void enviarSismografoAReparar(Estado estadoFs) {
        EstacionSismologica estacionSelected = this.getEstacionSismologica();
        estacionSelected.enviarSismografoAReparar(estadoFs);
    }

    public LocalDateTime obtenerFechaFinalizacion() {
        LocalDateTime fechaFinalizacion = null;

        for (CambioEstado cambio : cambiosEstados) {
            if (cambio.esFinalizado()) {
                fechaFinalizacion = cambio.getFechaHorafin();
                break;
            }
        }
        return fechaFinalizacion;
    };

    public CambioEstado obtenerCambioEstadoActual() {
        return cambiosEstados.stream()
            .filter(cambio -> cambio.getFechaHorafin() == null)
            .findFirst()  // Get the first (debería ser el unico)
            .orElse(null);
    }

    public Boolean compareNroOrder(Long number){
        if(this.getNumeroOrden().equals(number)){
            return true;
        }
        else {
            return false;
        }
    }

    public String toStringForPantalla() {
        // Obtener el cambio de estado actual (donde fechaHorafin es null)
        CambioEstado estadoActual = cambiosEstados.stream()
                .filter(cambio -> cambio.getFechaHorafin() == null)
                .findFirst()
                .orElse(null);

        // Format the date if available
        String fechaFin = (estadoActual != null && estadoActual.getFechaHorainicio() != null)
                ? estadoActual.getFechaHorainicio().toString()
                : "No disponible";

        return String.format(
                "\033[95m____________________________________________________________________________%n\033[0m" +
                "\033[92mNúmero de Orden:\033[0m %d%n" +
                        "\033[92mEstado:\033[0m %s%n" +
                        "\033[92mFecha de Finalización:\033[0m %s%n" +
                        "\033[92mEstación Sismológica:\033[0m %s%n" +
                        "\033[92mSismógrafo:\033[0m %s%n" +
                "\033[95m____________________________________________________________________________%n\033[0m"
                ,
                numeroOrden,
                this.obtenerCambioEstadoActual().estadoNuevo.getNombre(),
                fechaFin,
                estacionSismologica.getNombreEstacion(),
                estacionSismologica.getSismografo().getIdSismografo()

        );
    }

    // Dentro de la clase entity/OrdenInspeccion.java

    /**
     * Aplicación de Patron GRASP Experto y "Tell, Don't Ask".
     * Esta orden es la experta en su propia información.
     * Genera el DTO para la notificación, recibiendo los datos
     * que solo el Gestor conoce (el nuevo estado y los motivos de la GUI).
     */
    public DatosNotificacionCierre generarDatosNotificacion(String sismografoEstadoActual, List<MotivoFueraServicio> motivos, Empleado responsable) {

        // Ahora SÍ, la Orden es la experta en sus propios datos
        String sismografoId = this.estacionSismologica.getSismografo().getIdSismografo().toString();
        LocalDateTime fechaHora = this.obtenerCambioEstadoActual().getFechaHorainicio();
        Long numeroOrden = this.getNumeroOrden();
        String nombreEstacion = this.estacionSismologica.getNombreEstacion();
        String nombreResponsable = responsable.getNombreEmpleado(); // O .nombreEmpleado

        return new DatosNotificacionCierre(
                sismografoId,
                sismografoEstadoActual, // <-- Dato que pasó el Gestor
                fechaHora,
                motivos,                // <-- Dato que pasó el Gestor
                numeroOrden,
                nombreEstacion,
                nombreResponsable
        );
    }


}
