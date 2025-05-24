package control;

import entity.*;

import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MOCKDATAGenerator {

    private Long contadorOrdenInspeccion = 0L;

    private Long registrarCreacionOrdenInspeccion() {
        contadorOrdenInspeccion++;
        return contadorOrdenInspeccion;
    }

    private Long contadorSismografo = 0L;

    private Long registrarCreacionSismografo() {
        contadorSismografo++;
        return contadorSismografo;
    }



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

    public EstacionSismologica generarEstacionSismologica(int i) {

        // la estacion sismologica de ejemplo 1
        if  (i == 1) {Sismografo sismografo = new Sismografo(contadorSismografo, "ZETLAB", "Modelo 1", new Estado("Sismografo", null));
            return new EstacionSismologica("Estación Córdoba", sismografo);}

        // la estacion sismologica de ejemplo 1
        if  (i == 2) {Sismografo sismografo = new Sismografo(registrarCreacionSismografo(), "ZETLAB", "Modelo 1", new Estado("Sismografo", null));
            return new EstacionSismologica("Estación Buenos Aires", sismografo);}
        return null;
    }
    
    public List<OrdenInspeccion> generarOrdenesInspeccion(Empleado empleado, EstacionSismologica estacionSismologica, List<Estado> estadosOI) {
        List<OrdenInspeccion> ordenesInspeccion = List.of(

                // esta orden de inspeccion estaria en estado finalizada
                new OrdenInspeccion(registrarCreacionOrdenInspeccion(), estacionSismologica, List.of(), empleado,  "No hay Observaciones todavía", List.of(
                        new CambioEstado(Long.valueOf(1), null, estadosOI.get(9), LocalDateTime.now(), LocalDateTime.now(), empleado, List.of()),
                        new CambioEstado(Long.valueOf(2), estadosOI.get(9), estadosOI.get(10), LocalDateTime.now(), LocalDateTime.now(), empleado, List.of()),
                        new CambioEstado(Long.valueOf(3), estadosOI.get(10), estadosOI.get(11), LocalDateTime.now(), LocalDateTime.now(), empleado, List.of()),
                        new CambioEstado(Long.valueOf(4), estadosOI.get(11), estadosOI.get(12), LocalDateTime.now(), null, empleado, List.of())
                )),

                // esta orden de inspeccion estaria en estado pteRealizacion
                new OrdenInspeccion(Long.valueOf(registrarCreacionOrdenInspeccion()), estacionSismologica,  List.of(),empleado, "No hay Observaciones todavía", List.of(
                        new CambioEstado(Long.valueOf(1), null, estadosOI.get(9), LocalDateTime.now(), LocalDateTime.now(), empleado, List.of())
                )),

                // esta orden de inspeccion estaria en estado parcialmenteRealizada
                new OrdenInspeccion(Long.valueOf(registrarCreacionOrdenInspeccion()), estacionSismologica,  List.of(), empleado,"No hay Observaciones todavía", List.of(
                        new CambioEstado(Long.valueOf(1), null, estadosOI.get(9), LocalDateTime.now(), LocalDateTime.now(), empleado, List.of()),
                        new CambioEstado(Long.valueOf(2), estadosOI.get(9), estadosOI.get(10), LocalDateTime.now(), LocalDateTime.now(), empleado, List.of())
                ))
        );
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
