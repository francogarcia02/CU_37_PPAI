package entity;

import lombok.Data;

import java.util.List;

@Data
public class OrdenInspeccion {
    private Long numeroOrden;
    private EstacionSismologica estacionSismologica;
    private Empleado responsableOrdenInspeccion;
    private List<TareaTecnicaRevision> tareasTecnicasRevisiones;
    private String observaciones;
    private List<CambioEstado> cambiosEstados;

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
        this.tareasTecnicasRevisiones = tareasTecnicasRevisiones;
        this.responsableOrdenInspeccion = responsableOrdenInspeccion;
        this.observaciones = observaciones;
        this.cambiosEstados = cambiosEstados;
    }



    public Boolean sosFinalizada() {
        return cambiosEstados.stream()
            .filter(cambio -> cambio.getFechaHorafin() == null)  // Find current state (no end date)
            .findFirst()  // Get the first (debería ser el unico)
            .map(cambioActual -> "finalizada".equals(cambioActual.getEstadoNuevo().getNombre()))
            .orElse(false);  // Return false if no current state is found
    }

    public Boolean esTuRI(Empleado empleado) {
          return responsableOrdenInspeccion.equals(empleado);
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
                "____________________________________________________________________________%n" +
                "Número de Orden: %d%n" +
                        "Fecha de Finalización: %s%n" +
                        "Estación Sismológica: %s%n" +
                        "Sismógrafo: %s%n" +
                "____________________________________________________________________________%n"
                ,
                numeroOrden,
                fechaFin,
                estacionSismologica.getNombreEstacion(),
                estacionSismologica.getSismografo().getIdSismografo()

        );
    }
}
