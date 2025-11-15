package control;

import control.persistencia.OrdenDAO;
import entity.Empleado;
import entity.OrdenInspeccion;
import entity.Sesion;
import entity.Usuario;
import entity.TipoMotivo;
import entity.Estado;
import entity.MotivoFueraServicio;
import interfaces.GestorOrdenInterface;
import lombok.Data;
import control.notificacion.DatosNotificacionCierre; // aplicacion patron observer
import control.notificacion.IObservadorCierreOrden; // aplicacion patron observer
import control.notificacion.ISujetoCierreOrden; // aplicacion patron observer
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class GestorOrden implements GestorOrdenInterface , ISujetoCierreOrden {

    private Usuario usuarioLogueado;
    private Empleado RI;
    private List<OrdenInspeccion> ordenesInspeccion;
    private OrdenInspeccion selectedOrden;

    private String selectedDecicionSismografo;
    private String observaciones;
    private List<Empleado> empleados;
    private List<TipoMotivo> tiposMotivos;
    private List<MotivoFueraServicio> motivosFueraServicioSelection = new ArrayList<>();
    private List<Estado> estados;
    private Estado EstadoFS;
    private Estado EstadoCerrada;
    private Boolean confirmacionCierre;
    private List<OrdenInspeccion> ordenesInspeccionFiltradas = new ArrayList<>();
    private Sesion sesion;
    private OrdenDAO ordenDAO; // PERSISTENCIA

    // --- INICIA EL PATRÓN OBSERVER ---
    private List<IObservadorCierreOrden> observadores = new ArrayList<>();

    @Override
    public void agregarObservador(IObservadorCierreOrden observador) {
        observadores.add(observador);
    }

    @Override
    public void quitarObservador(IObservadorCierreOrden observador) {
        observadores.remove(observador);
    }

    @Override
    public void notificar(DatosNotificacionCierre datos) {
        for (IObservadorCierreOrden obs : observadores) {
            obs.actualizar(datos);
        }
    }
    // --- FIN PATRÓN OBSERVER ---

    public GestorOrden(List<Empleado> empleados, List<TipoMotivo> tiposMotivos, List<Estado> estados, Sesion sesion) {
        // this.ordenesInspeccion = ordenesInspeccion; // No es necesaria, por implementacion de Lectura en BD
        this.empleados = empleados;
        this.tiposMotivos = tiposMotivos;
        this.estados = estados;
        this.sesion = sesion;
        this.ordenDAO = new control.persistencia.OrdenDAOImpl(); //Inicializar para PERSISTENCIA
    }

    @Override
    public Empleado buscarEmpleado() {
        usuarioLogueado = obtenerUsuarioLogueado();
        RI = usuarioLogueado.getEmpleado();
        return RI;
    }

// En GestorOrden.java


    /*
    @Override
    public List<OrdenInspeccion> buscarOrdenesInspeccion() {
        ordenesInspeccionFiltradas.clear();

        // USABA LISTA DE ORDENES DEL MOCK
        ordenesInspeccion.forEach(ordenInspeccion -> {
            // Chequea que la orden esté en el estado correcto
            boolean condition1 = ordenInspeccion.estaFinalizada();
            // Chequea que la orden pertenezca al Responsable de Inspección logueado
            boolean condition2 = ordenInspeccion.esTuRI(RI);

            if (condition1 && condition2) {
                ordenesInspeccionFiltradas.add(ordenInspeccion);
            }
        });

        // Devuelve la lista filtrada y ordenada
        return ordenarOI(ordenesInspeccionFiltradas);
    }
    */ // Metodo buscarOrdenesInspeccion() SIN PERSISTENCIA

    @Override
    public List<OrdenInspeccion> buscarOrdenesInspeccion() {

        // El 'RI' (Responsable de Inspección) debe estar seteado
        // Asumimos que la pantalla llamó a "buscarEmpleado()" primero
        if (RI == null) {
            // Opcional: llamar a buscarEmpleado() aquí si no se ha hecho
            buscarEmpleado();
        }

        // 1. Llamada al DAO para obtener las órdenes REALES de la BD
        List<OrdenInspeccion> ordenesDesdeBD = ordenDAO.buscarFinalizadasPorRI(RI);

        // 2. El metodo de ordenar ahora trabaja sobre la lista de la BD
        return ordenarOI(ordenesDesdeBD);
    }

    @Override
    public List<OrdenInspeccion> ordenarOI(List<OrdenInspeccion> ordenesInspeccionToOrder) {
        return ordenesInspeccionToOrder.stream()
                .sorted((o1, o2) -> {
                    LocalDateTime fecha1 = o1.obtenerFechaFinalizacion();
                    LocalDateTime fecha2 = o2.obtenerFechaFinalizacion();
                    if (fecha1 == null && fecha2 == null) return 0;
                    if (fecha1 == null) return 1;
                    if (fecha2 == null) return -1;
                    return fecha1.compareTo(fecha2);
                })
                .collect(Collectors.toList());
    }

    @Override
    public String stringificarOI(OrdenInspeccion ordenInspeccionToStringify) {
        return ordenInspeccionToStringify.toStringForPantalla();
    }

    @Override
    public void tomarNumeroOI(Long selectedOrdenNumero) {
        selectedOrden = ordenesInspeccionFiltradas.stream()
                .filter(oi -> oi.getNumeroOrden().equals(selectedOrdenNumero))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void tomarDatosObservacion(String observacion) {
        setObservaciones(observacion);
    }

    @Override
    public void tomarSeleccionDecicionSismografo(String selectedDecicionSismografo) {
        setSelectedDecicionSismografo(selectedDecicionSismografo);
    }

    @Override
    public List<String> stringificarMFS() {
        List<String> stringifiedMFS = new ArrayList<>();
        for (int i = 0; i < this.getTiposMotivos().size(); i++) {
            TipoMotivo tipoMotivo = this.getTiposMotivos().get(i);
            stringifiedMFS.add((i + 1) + ": " + tipoMotivo.getDescripcion());
        }
        return stringifiedMFS;
    }

    @Override
    public void tomarMFSyComentario(TipoMotivo motivoSeleccionado, String comentario) {
        this.getMotivosFueraServicioSelection().add(new MotivoFueraServicio(comentario, motivoSeleccionado));
    }

    @Override
    public void tomarConfirmacioncierreOI(Boolean input) {
        if(selectedOrden != null){
            setConfirmacionCierre(input);
        }
    }

    @Override
    public void buscarEstadoFS() {
        estados.stream()
                .filter(estado -> estado.esAmbitoSismografo() && estado.esFueraDeServicio())
                .findFirst()
                .ifPresent(this::setEstadoFS);
    }

    @Override
    public void buscarEstadoCerradoOI() {
        estados.stream()
                .filter(estado -> estado.esAmbitoOrdendeInspeccion() && estado.esCerrada())
                .findFirst()
                .ifPresent(this::setEstadoCerrada);
    }

    public boolean cerrarOrdenSeleccionada() {
        if (getConfirmacionCierre() && getObservaciones() != null) {
            buscarEstadoFS();
            buscarEstadoCerradoOI(); // Este metodo setea el atributo 'EstadoCerrada'

            if (getEstadoCerrada() == null) {
                System.err.println("ERROR: No se pudo encontrar el estado 'CierreDefinitivo' en la lista de estados.");
                return false;
            } //Importante asegurarnos de encontrar el estado.

            boolean result = getSelectedOrden().cerrar(
                    getObservaciones(),
                    getMotivosFueraServicioSelection(),
                    getEstadoCerrada(), // Asumiendo que el estado 13 es "cierreDefinitivo"
                    getRI());

            String sismografoEstadoActual;
            // Logica para determinar el estado final.
            if (!getMotivosFueraServicioSelection().isEmpty()) {
                getSelectedOrden().enviarSismografoAReparar(getEstadoFS());
                sismografoEstadoActual = getEstadoFS().getNombre(); // "Fuera de Servicio"
            } else {
                // Si no hay motivos, quedará online (o el estado que .cerrar() le haya puesto)
                sismografoEstadoActual = getSelectedOrden().getEstacionSismologica().getSismografo().getEstadoActual().getNombre();
            }

            // DEBUG
            //System.out.println("--- DEBUG: Motivos a notificar: " + getMotivosFueraServicioSelection().size() + " ---");

            // --- INICIO PERSISTENCIA ---
            // Le pasamos la orden (que ya tiene su estado "CierreDefinitivo")
            // el estado del sismógrafo (si es que cambió) y la lista de motivos
            boolean guardadoOK = ordenDAO.guardarCierre(
                    getSelectedOrden(),
                    getEstadoFS(), // Le pasamos el objeto Estado "Fuera de Servicio"
                    getMotivosFueraServicioSelection()
            );

            if (!guardadoOK) {
                // (Manejar error)
                System.err.println("¡ERROR AL GUARDAR EN LA BASE DE DATOS!");
            }
            // --- FIN PERSISTENCIA ---

            // --- INICIO "DISPARADOR" OBSERVER ---

            // Anterior implementacion:
            /* 1. Creamos el DTO con toda la info que los observadores puedan necesitar
//            DatosNotificacionCierre datos = new DatosNotificacionCierre(
//                    getSelectedOrden().getEstacionSismologica().getSismografo().getIdSismografo().toString(),
//                    sismografoEstadoActual, // El estado final real
//                    getSelectedOrden().obtenerCambioEstadoActual().getFechaHorainicio(),
//                    getMotivosFueraServicioSelection(), // Pasamos la lista (puede estar vacía)
//                    getSelectedOrden().getNumeroOrden(),
//                    getSelectedOrden().getEstacionSismologica().getNombreEstacion(),
//                    getRI().getNombreEmpleado()
//            );
             */

            // Nueva Implementacion:
            // "Tell, Dont Ask": Le "decimos" a la orden que genere el DTO.
            //  Le pasamos solo la información que el Gestor tiene y la Orden no.
            /*
            * Beneficios:
            * - Bajo Acoplamiento: el GestorOrden no conoce
            *  detalles concretos sobre la EstacionSismologica o Sismografo.
            * - Alta Cohesion: Se mantiene la logica de "juntar datos de una orden" dentro de la propia orden.
            * - Mantenibilidad: Si cambia la estructura, solo debo modificar OrdenInspeccion, y no el Gestor.
            * */
            DatosNotificacionCierre datos = getSelectedOrden().generarDatosNotificacion(
                    sismografoEstadoActual,
                    getMotivosFueraServicioSelection(),
                    getRI() // Pasamos el empleado logueado
            );

            // 2. Notificamos (SIEMPRE, al final del cierre)
            this.notificar(datos);
            // --- FIN DISPARADOR OBSERVER ---

            // publicarMonitores(); METODO QUE YA NO USAMOS, DES-ACOPLO EL GESTOR.
            return result;
        }
        return false;
    }

    //Responsabilidad que el gestor MANTIENE
    @Override
    public List<String> obtenerMailsResponsablesReparacion() {
        return empleados.stream()
                .filter(Empleado::esResponsableReparaciones)
                .map(Empleado::obtenerMail)
                .collect(Collectors.toList());
    }

// Responsabilidad que el gestor no conserva. Por aplicacion del Patron Observer.
    //@Override
//    public void enviarNotificacionMail(String mensaje) {
//        List<String> mails = obtenerMailsResponsablesReparacion();
//        InterfazMail interfazMail = new InterfazMail();
//        mails.forEach(mail -> {
//            System.out.println(interfazMail.enviarMail(mail, mensaje)); // Placeholder para el envío real
//        });
//    }

// Responsabilidad que el gestor no conserva. Por aplicacion del Patron Observer.
//    public void publicarMonitores() {
//        InterfazCCRS interfazCCRS = new InterfazCCRS();
//        interfazCCRS.imprimirMonitores();
//        System.out.println("Publicación de monitores CCRS completada"); // Placeholder
//    }

// Responsabilidad que el gestor no conserva. Por aplicacion del Patron Observer.
//    public String confeccionarMensaje(OrdenInspeccion orden) {
//        String motivosStr = orden.obtenerCambioEstadoActual().getMotivosCambioEstados() != null ?
//                orden.obtenerCambioEstadoActual().getMotivosCambioEstados().stream()
//                        .map(motivo -> String.format("  - Motivo: %s\n    Observaciones: %s\n",
//                                motivo.getTipoMotivo().getDescripcion(),
//                                motivo.getComentario() != null ? motivo.getComentario() : "Sin observaciones"))
//                        .collect(Collectors.joining("\n")) :
//                "  No hay motivos registrados\n";
//
//        return String.format(
//                "Estimado(a) responsable de reparaciones,\n\n" +
//                        "La Orden de Inspeccion %d ha sido cerrada.\n\n" +
//                        "Detalles:\n" +
//                        "Estación Sismológica: %s\n" +
//                        "Responsable de la Orden: %s\n" +
//                        "ID sismógrafo: %d\n\n" +
//                        "Estado actual del sismógrafo: %s\n" +
//                        "Fecha y hora nuevo estado: %s\n" +
//                        "Motivos:\n%s",
//                orden.getNumeroOrden(),
//                orden.getEstacionSismologica().getNombreEstacion(),
//                orden.getResponsableOrdenInspeccion().getNombreEmpleado(),
//                orden.getEstacionSismologica().getSismografo().getIdSismografo(),
//                orden.getEstacionSismologica().getSismografo().getEstadoActual().getNombre(),
//                orden.obtenerCambioEstadoActual().getFechaHorainicio(),
//                motivosStr
//        );
//    }

    @Override
    public Usuario obtenerUsuarioLogueado() {
        return sesion.getUsuario();
    }

    // --- Métodos de la interfaz que ya no son necesarios o han sido adaptados ---

    @Override
    public void RecibirSelectedOption(String selectedOption) {
        // La lógica de opciones se manejará en el controlador de la GUI
    }

    @Override
    public void manageSismografoFS() {
        // La lógica de selección de motivos se manejará en el controlador de la GUI
    }

    @Override
    public Boolean validarMotivo() {
        // La validación se puede hacer en el controlador antes de llamar a tomarMFSyComentario
        return true;
    }

    @Override
    public LocalDateTime getFechaHoraActual() {
        return LocalDateTime.now();
    }

    @Override
    public void FinCU() {
        // La gestión del fin del caso de uso la hará la GUI (ej. cerrar ventana)
    }

    @Override
    public void RecibirTipoMotivos(List<TipoMotivo> listaMotivos) {
        setTiposMotivos(listaMotivos);
    }

    @Override
    public void pasarToPantallaOIs() {
        // No es necesario, el controlador obtendrá la lista y la mostrará.
    }
}