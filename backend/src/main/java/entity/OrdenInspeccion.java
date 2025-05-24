package entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrdenInspeccion {
    private Long numeroOrden;
    private EstacionSismologica estacionSismologica;
    private Empleado responsableOrdenInspeccion;
    private List<TareaTecnicaRevision> tareasTecnicasRevisiones;
    private String observaciones;
    private List<CambioEstado> cambiosEstados;


    public Boolean cerrar(String observacion, List<MotivoFueraServicio> motivosNuevos, Estado estadoCerrada, Empleado responsableEjecucion) {
        for (CambioEstado cambio : cambiosEstados) {
            if (cambio.getFechaHorafin() == null &&
                "finalizada".equalsIgnoreCase(cambio.getEstadoNuevo().getNombre())) {
                this.setObservaciones(observacion);
                List<MotivoFueraServicio> lista = new ArrayList<>();
                lista.addAll(cambio.getMotivosCambioEstados()); 
                lista.addAll(motivosNuevos);
                cambio.setMotivosCambioEstados(lista);
                cambio.setFechaHorafin(LocalDateTime.now());
                cambiosEstados.add(
                        new CambioEstado(
                                cambio.getIdCambioEstado() + 1L,
                                cambio.getEstadoNuevo(),
                                estadoCerrada,
                                LocalDateTime.now(),
                                null,
                                responsableEjecucion,
                                null
                ));
                return true;
            }
        }
        return false;
    }

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

    public String getResponsableDeInspeccion(){
        String nombre = this.responsableOrdenInspeccion.getNombreEmpleado();
        System.out.println(nombre);
        return nombre;
    }

    public String obtenerFechaFinalizacionMasReciente() {
        LocalDateTime fechaMasReciente = null;

        for (CambioEstado cambio : cambiosEstados) {
            LocalDateTime fechaFin = cambio.getFechaHorafin();
            if (fechaFin != null) {
                if (fechaMasReciente == null || fechaFin.isAfter(fechaMasReciente)) {
                    fechaMasReciente = fechaFin;
                }
            }
        }

        if (fechaMasReciente != null) {
            System.out.println(fechaMasReciente.toString()); // o formatealo como desees
        } else {
            System.out.println("No finalizada");
        }
        return ("");
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
                        "\033[92mFecha de Finalización:\033[0m %s%n" +
                        "\033[92mEstación Sismológica:\033[0m %s%n" +
                        "\033[92mSismógrafo:\033[0m %s%n" +
                "\033[95m____________________________________________________________________________%n\033[0m"
                ,
                numeroOrden,
                fechaFin,
                estacionSismologica.getNombreEstacion(),
                estacionSismologica.getSismografo().getIdSismografo()

        );
    }
}
