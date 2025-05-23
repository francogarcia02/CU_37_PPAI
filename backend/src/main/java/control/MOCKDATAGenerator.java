package control;

import entity.*;

import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.Date;
import java.util.List;

public class MOCKDATAGenerator {

    public Empleado generarEmpleado(int i) {
        if (i == 1) {
            return new Empleado(Long.valueOf(1), "John", "Doe", Rol.RESPONSABLE_INSPECCIONES, generarUsuario(1));
        }

        if (i == 2) {
            return new Empleado(Long.valueOf(2), "Jane", "Doe", Rol.ADMIN, generarUsuario(2));
        }
        return null;
    }

    public Usuario generarUsuario(int i) {
       if (i == 1) {
           return new Usuario("JohnDoe", "123456", "j0o2k@example.com");
       }

       if (i == 2) {
           return new Usuario("JaneDoe", "123456", "j0o2k@example.com");
       }
       return null;
    }

    public Sesion generarSesion(Usuario usuario) {
        return new Sesion(usuario);
    }

    public List<Estado> generarEstadosOI() {
        return List.of(
                new Estado("ORDEN_INSPECCION", "pteRealizacion"),
                new Estado("ORDEN_INSPECCION", "parcialmenteRealizada"),
                new Estado("ORDEN_INSPECCION", "realizada"),
                new Estado("ORDEN_INSPECCION", "finalizada"),
                new Estado("ORDEN_INSPECCION", "cierreDefinitivo"));
    }

    public EstacionSismologica generarEstacionSismologica() {
        return new EstacionSismologica("Estacion 1", new Sismografo(Long.valueOf(1), "ZETLAB", "Modelo 1"));
    };
    public List<OrdenInspeccion> generarOrdenesInspeccion(Empleado empleado, EstacionSismologica estacionSismologica, List<Estado> estadosOI) {
        List<OrdenInspeccion> ordenesInspeccion = List.of(

                // esta orden de inspeccion estaria en estado finalizada
                new OrdenInspeccion(Long.valueOf(1), estacionSismologica, List.of(), empleado,  "No hay Observaciones todavía", List.of(
                        new CambioEstado(Long.valueOf(1), null, estadosOI.get(0), LocalDateTime.now(), LocalDateTime.now(), empleado, List.of()),
                        new CambioEstado(Long.valueOf(2), estadosOI.get(0), estadosOI.get(1), LocalDateTime.now(), LocalDateTime.now(), empleado, List.of()),
                        new CambioEstado(Long.valueOf(3), estadosOI.get(1), estadosOI.get(2), LocalDateTime.now(), LocalDateTime.now(), empleado, List.of()),
                        new CambioEstado(Long.valueOf(4), estadosOI.get(2), estadosOI.get(3), LocalDateTime.now(), null, empleado, List.of())
                )),

                // esta orden de inspeccion estaria en estado pteRealizacion
                new OrdenInspeccion(Long.valueOf(2), estacionSismologica,  List.of(),empleado, "No hay Observaciones todavía", List.of(
                        new CambioEstado(Long.valueOf(1), null, estadosOI.get(0), LocalDateTime.now(), LocalDateTime.now(), empleado, List.of())
                )),

                // esta orden de inspeccion estaria en estado parcialmenteRealizada
                new OrdenInspeccion(Long.valueOf(3), estacionSismologica,  List.of(), empleado,"No hay Observaciones todavía", List.of(
                        new CambioEstado(Long.valueOf(1), null, estadosOI.get(0), LocalDateTime.now(), LocalDateTime.now(), empleado, List.of()),
                        new CambioEstado(Long.valueOf(2), estadosOI.get(0), estadosOI.get(1), LocalDateTime.now(), LocalDateTime.now(), empleado, List.of())
                )));
        return ordenesInspeccion;
    }



}
