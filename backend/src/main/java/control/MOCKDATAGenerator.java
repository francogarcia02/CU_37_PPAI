package control;

import entity.*;

import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.ArrayList;
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

    public List<Estado> generarEstados() {
        return List.of(
                new Estado("SISMOGRAFO", "pteCertificacion"),
                new Estado("SISMOGRAFO", "Disponible"),
                new Estado("SISMOGRAFO", "enInstalacion"),
                new Estado("SISMOGRAFO", "enRevicion"),
                new Estado("SISMOGRAFO", "Descartado"),
                new Estado("SISMOGRAFO", "enReparacion"),
                new Estado("SISMOGRAFO", "enLinea"),
                new Estado("SISMOGRAFO", "Inhabilitado"),
                new Estado("SISMOGRAFO", "fueraServicio"),
                new Estado("ORDEN_INSPECCION", "pteRealizacion"),
                new Estado("ORDEN_INSPECCION", "parcialmenteRealizada"),
                new Estado("ORDEN_INSPECCION", "realizada"),
                new Estado("ORDEN_INSPECCION", "finalizada"),
                new Estado("ORDEN_INSPECCION", "cierreDefinitivo"));
    }

    public EstacionSismologica generarEstacionSismologica() {
        Sismografo sismografo = new Sismografo(1L, "ZETLAB", "Modelo 1", new Estado("Sismografo", null));
        return new EstacionSismologica("Estación 1", sismografo);
    }
    
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
    public List<TipoMotivo> generarTipoMotivo(){
        return List.of(
                new TipoMotivo("Avería por vibración"),
                new TipoMotivo("Desgaste de componente"),
                new TipoMotivo("Fallo en el sistema de registro"),
                new TipoMotivo("Vandalismo"),
                new TipoMotivo("Fallo en fuente de alimentación"));
    }


}
