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
            return new Empleado(Long.valueOf(1), "Agustin", "Bieber", Rol.RESPONSABLE_INSPECCIONES, "agustinbieber@gmail.com", "3512345671");
        }

        if (i == 2) {
            return new Empleado(Long.valueOf(2), "Jane", "Doe", Rol.RESPONSABLE_REPARACIONES, "janeDoe@gmail.com", "3517654321");
        }
        return null;
    }

    public Usuario generarUsuario(int i) {
       if (i == 1) {
           return new Usuario("AgusBieberQW", "123456", generarEmpleado(1));
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
        // Los IDsDEBEN coincidir con los IDs que insertamos en el script de SQL.
        return List.of(
                // Estados de Sismógrafo (IDs 1, 2, 3 de la BD)
                new Estado(6, "SISMOGRAFO", "enLinea"), // Asumiendo ID 6
                new Estado(7, "SISMOGRAFO", "Inhabilitado"), // Asumiendo ID 7
                new Estado(3, "SISMOGRAFO", "Fuera de Servicio"), // ID3
                new Estado(8, "SISMOGRAFO", "pteCertificacion"),
                new Estado(9, "SISMOGRAFO", "Disponible"),
                new Estado(10, "SISMOGRAFO", "enInstalacion"),
                new Estado(11, "SISMOGRAFO", "enRevicion"),
                new Estado(12, "SISMOGRAFO", "Descartado"),
                new Estado(13, "SISMOGRAFO", "enReparacion"),

                // Estados de OrdenInspeccion (IDs 4, 5, 6, 7 de la BD)
                new Estado(4, "ORDEN_INSPECCION", "PteRealización"), // CORREGIDO
                new Estado(5, "ORDEN_INSPECCION", "ParcialmenteRealizado"), // CORREGIDO
                new Estado(6, "ORDEN_INSPECCION", "Finalizado"), // ID=6
                new Estado(7, "ORDEN_INSPECCION", "CierreDefinitivo") // ID=7
        );
    }

    public EstacionSismologica generarEstacionSismologica(int i) {

        // la estacion sismologica de ejemplo 1
        if  (i == 1) {
            // Asumiendo que "enLinea" es el ID 6
            Estado estadoEnLinea = new Estado(6, "Sismografo", "enLinea");
            Sismografo sismografo = new Sismografo(contadorSismografo, "12-12-2024", 12,"ZETLAB", "Modelo 1", estadoEnLinea);
            return new EstacionSismologica(1 ,"Estación Sierra de la Invernada",10, 20, 5, "20-12-2024", "Documentacion", sismografo);
        }

        // la estacion sismologica de ejemplo 1
        if  (i == 2) {
            Estado estadoEnLinea = new Estado(6, "Sismografo", "enLinea");
            Sismografo sismografo = new Sismografo(registrarCreacionSismografo(), "12-12-2024", 11,"ZETLAB", "Modelo 1", estadoEnLinea);
            return new EstacionSismologica(2, "Estación San Luis", 11, 23, 20, "13-03-2023", "Documentacion", sismografo);
        }
        return null;
    }

    public List<OrdenInspeccion> generarOrdenesInspeccion(Empleado empleado, EstacionSismologica estacionSismologica, List<Estado> estadosOI) {

        // Para que sea más legible, vamos a buscar los estados por nombre
        // Esta vez, nos aseguramos de que existan en tu lista de 'generarEstados'.
        Estado pteRealizacion = estadosOI.stream()
                .filter(e -> e.nombre.equals("PteRealización") && e.ambito.equals("ORDEN_INSPECCION"))
                .findFirst().orElse(null);
        Estado parcRealizado = estadosOI.stream()
                .filter(e -> e.nombre.equals("ParcialmenteRealizado") && e.ambito.equals("ORDEN_INSPECCION"))
                .findFirst().orElse(null);
        Estado finalizado = estadosOI.stream()
                .filter(e -> e.nombre.equals("Finalizado") && e.ambito.equals("ORDEN_INSPECCION"))
                .findFirst().orElse(null); // ¡EL QUE BUSCAMOS!

        // --- Verificación de seguridad ---
        // Si alguno es nulo, significa que los strings de arriba no coinciden
        // con los de tu metodo generarEstados().
        if (pteRealizacion == null || parcRealizado == null || finalizado == null) {
            System.err.println("FATAL MOCK ERROR: No se pudieron encontrar los estados básicos (PteRealización, ParcialmenteRealizado, Finalizado) en la lista de estados.");
            // Devolvemos una lista vacía para evitar el crash
            return new ArrayList<>();
        }

        List<OrdenInspeccion> ordenesInspeccion = List.of(

                // --- ORDEN 1: ¡ESTA SÍ VA A FUNCIONAR! ---
                // esta orden de inspeccion estaria en estado "Finalizado"
                new OrdenInspeccion(registrarCreacionOrdenInspeccion(), estacionSismologica, List.of(), empleado, "No hay Observaciones todavía", List.of(
                        // Estado anterior (pteRealizacion) - CERRADO
                        new CambioEstado(1L, null, pteRealizacion, LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(1), empleado, List.of()),
                        // Estado anterior (ParcialmenteRealizado) - CERRADO
                        new CambioEstado(2L, pteRealizacion, parcRealizado, LocalDateTime.now().minusDays(1), LocalDateTime.now(), empleado, List.of()),
                        // --- ESTADO ACTUAL (Finalizado) - ABIERTO (fechaHorafin = null) ---
                        new CambioEstado(3L, parcRealizado, finalizado, LocalDateTime.now(), null, empleado, List.of())
                )),

                // --- ORDEN 2: Estará en "PteRealización" ---
                // esta orden de inspeccion estaria en estado pteRealizacion
                new OrdenInspeccion(registrarCreacionOrdenInspeccion(), estacionSismologica, List.of(), empleado, "No hay Observaciones todavía", List.of(
                        // --- ESTADO ACTUAL (PteRealización) - ABIERTO (fechaHorafin = null) ---
                        new CambioEstado(4L, null, pteRealizacion, LocalDateTime.now(), null, empleado, List.of())
                )),

                // --- ORDEN 3: Estará en "ParcialmenteRealizado" ---
                // esta orden de inspeccion estaria en estado parcialmenteRealizada
                new OrdenInspeccion(registrarCreacionOrdenInspeccion(), estacionSismologica, List.of(), empleado, "No hay Observaciones todavía", List.of(
                        // Estado anterior (pteRealizacion) - CERRADO
                        new CambioEstado(5L, null, pteRealizacion, LocalDateTime.now().minusDays(1), LocalDateTime.now(), empleado, List.of()),
                        // --- ESTADO ACTUAL (ParcialmenteRealizado) - ABIERTO (fechaHorafin = null) ---
                        new CambioEstado(6L, pteRealizacion, parcRealizado, LocalDateTime.now(), null, empleado, List.of())
                ))
        );
        return ordenesInspeccion;
    }

    public List<TipoMotivo> generarTipoMotivo(){
        return List.of(
                // Los IDs coinciden con los IDENTITY(1,1) del script SQL
                new TipoMotivo(1, "Avería por vibración"),
                new TipoMotivo(2, "Desgaste de componente"),
                new TipoMotivo(3, "Fallo en el sistema de registro"),
                new TipoMotivo(4, "Vandalismo"),
                new TipoMotivo(5, "Fallo en fuente de alimentación"));
    }


}
