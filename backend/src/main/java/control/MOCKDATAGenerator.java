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
            return new Empleado(Long.valueOf(1), "John", "Doe", Rol.RESPONSABLE_INSPECCIONES, "johnDoe@gmail.com", "3512345671");
        }

        if (i == 2) {
            return new Empleado(Long.valueOf(2), "Jane", "Doe", Rol.RESPONSABLE_REPARACIONES, "janeDoe@gmail.com", "3517654321");
        }
        return null;
    }

    public Usuario generarUsuario(int i) {
       if (i == 1) {
           return new Usuario("JohnDoe", "123456", generarEmpleado(1));
       }

       if (i == 2) {
           return new Usuario("JaneDoe", "123456", generarEmpleado(2));
       }
       return null;
    }

    public Sesion generarSesion(Usuario usuario) {
        return new Sesion(usuario);
    }

    public List<Estado> generarEstados() {
        return List.of(
                new Estado("SISMOGRAFO", "pteCertificacion"), // 0
                new Estado("SISMOGRAFO", "Disponible"), // 1
                new Estado("SISMOGRAFO", "enInstalacion"), // 2
                new Estado("SISMOGRAFO", "enRevicion"), // 3
                new Estado("SISMOGRAFO", "Descartado"), // 4
                new Estado("SISMOGRAFO", "enReparacion"), // 5
                new Estado("SISMOGRAFO", "enLinea"), // 6
                new Estado("SISMOGRAFO", "Inhabilitado"), // 7
                new Estado("SISMOGRAFO", "fueraServicio"), // 8
                new Estado("ORDEN_INSPECCION", "pteRealizacion"), // 9
                new Estado("ORDEN_INSPECCION", "parcialmenteRealizada"), // 10
                new Estado("ORDEN_INSPECCION", "realizada"), // 11
                new Estado("ORDEN_INSPECCION", "finalizada"), // 12
                new Estado("ORDEN_INSPECCION", "cierreDefinitivo")); //13
    }

    public EstacionSismologica generarEstacionSismologica(int i) {

        // la estacion sismologica de ejemplo 1
        if  (i == 1) {Sismografo sismografo = new Sismografo(contadorSismografo, "12-12-2024", 12,"ZETLAB", "Modelo 1", new Estado("Sismografo", "enLinea"));
            return new EstacionSismologica(1 ,"Estación Córdoba",10, 20, 5, "20-12-2024", "Documentacion", sismografo);}

        // la estacion sismologica de ejemplo 1
        if  (i == 2) {Sismografo sismografo = new Sismografo(registrarCreacionSismografo(), "12-12-2024", 11,"ZETLAB", "Modelo 1", new Estado("Sismografo", "enLinea"));
            return new EstacionSismologica(2, "Estación Buenos Aires", 11, 23, 20, "13-03-2023", "Documentacion", sismografo);}
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
