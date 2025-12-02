package control;

import boundary.notificacion.InterfazCCRS;
import boundary.notificacion.InterfazMail;
import control.notificacion.DatosNotificacionCierre;
import control.notificacion.IObservadorCierreOrden;
import control.notificacion.ISujetoCierreOrden;
import control.persistencia.OrdenDAO;
import entity.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class GestorOrden implements ISujetoCierreOrden {

    // --- Atributos de Negocio y Estado ---
    private Usuario usuarioLogueado;
    private Empleado RI;
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
    private OrdenDAO ordenDAO;

    // --- PATRÓN OBSERVER ---
    private List<IObservadorCierreOrden> observadores = new ArrayList<>();

    // --- CONSTRUCTOR ---
    public GestorOrden(List<Empleado> empleados, List<TipoMotivo> tiposMotivos, List<Estado> estados, Sesion sesion, OrdenDAO ordenDAO) {
        this.empleados = empleados;
        this.tiposMotivos = tiposMotivos;
        this.estados = estados;
        this.sesion = sesion;
        this.ordenDAO = ordenDAO;
    }

    // --- IMPLEMENTACIÓN ISujetoCierreOrden ---
    @Override
    public void agregarObservador(IObservadorCierreOrden observador) {
        this.observadores.add(observador);
    }

    @Override
    public void quitarObservador(IObservadorCierreOrden observador) {
        this.observadores.remove(observador);
    }

    @Override
    public void notificar(Object datos, String evento) {
        for (IObservadorCierreOrden obs : observadores) {
            obs.actualizar(datos, evento);
        }
    }

    // ==========================================================================
    // === LÓGICA DE NEGOCIO (CU 37) ===
    // ==========================================================================

    public boolean cerrarOrdenSeleccionada() {
        if (confirmacionCierre && observaciones != null) {

            // 1. Lógica de Estados
            buscarEstadoFS();
            buscarEstadoCerradoOI();

            if (getEstadoCerrada() == null) return false;

            // 2. Delegación a la Entidad
            boolean cierreExitoso = getSelectedOrden().cerrar(
                    observaciones,
                    motivosFueraServicioSelection,
                    getEstadoCerrada(),
                    getRI()
            );

            if (cierreExitoso) {
                // 3. Manejo de Sismógrafo
                if (!motivosFueraServicioSelection.isEmpty()) {
                    getSelectedOrden().enviarSismografoAReparar(getEstadoFS());
                }

                // 4. Persistencia
                ordenDAO.update(getSelectedOrden());

                // 5. Logica de Notificación (Observer)

                // A. Generar el DTO con todos los datos adquiridos.
                DatosNotificacionCierre dto = this.generarDatosNotificacion();

                // B. Metodo de Control que dispara el Patron Observador.
                this.notificarObservadores(dto);

                return true;
            }
        }
        return false;
    }

    private DatosNotificacionCierre generarDatosNotificacion() {
        String idSismografo = getSelectedOrden().getIdSismografo();
        String nombreEstacion = getSelectedOrden().getNombreEstacion();
        String nombreResponsable = getRI().getNombreEmpleado();
        String estadoSismoActual = getSelectedOrden().getNombreEstadoSismografo();

        // Obtener Mails en lista de Strings.
        List<String> listaMails = obtenerMailsResponsablesReparacion();

        // Conversion a tipo primitivo String
        List<String> listaMotivosTexto = motivosFueraServicioSelection.stream()
                .map(m -> String.format("%s (%s)", m.getTipoMotivo().getDescripcion(),
                        (m.getComentario() != null ? m.getComentario() : "-")))
                .collect(Collectors.toList());

        return new DatosNotificacionCierre(
                listaMails, idSismografo, estadoSismoActual, LocalDateTime.now(),
                listaMotivosTexto, getSelectedOrden().getNumeroOrden(),
                nombreEstacion, nombreResponsable
        );
    }

    private void notificarObservadores(DatosNotificacionCierre dto) {
        // Creator: El Gestor crea sus observadores
        this.observadores.clear();
        IObservadorCierreOrden observadorMail = new InterfazMail();
        IObservadorCierreOrden observadorCCRS = new InterfazCCRS();

        //  Subscripcion de Observadores
        this.agregarObservador(observadorMail);
        this.agregarObservador(observadorCCRS);

        this.notificar(dto, "CIERRE_ORDEN");
    }

    public List<String> obtenerMailsResponsablesReparacion() {
        return empleados.stream()
                .filter(Empleado::esResponsableReparaciones)
                .map(Empleado::obtenerMail)
                .collect(Collectors.toList());
    }

    // --- MÉTODOS DE SOPORTE Y PANTALLA ---

    public Empleado buscarEmpleado() {
        usuarioLogueado = sesion.getUsuario();
        RI = usuarioLogueado.getEmpleado();
        return RI;
    }

    public List<OrdenInspeccion> buscarOrdenesInspeccion() {
        ordenesInspeccionFiltradas.clear();
        if (RI == null) buscarEmpleado();

        List<OrdenInspeccion> ordenesDesdeBD = ordenDAO.getAllOrdenes();
        ordenesDesdeBD.forEach(ordenInspeccion -> {
            if (ordenInspeccion.estaFinalizada() && ordenInspeccion.esTuRI(RI)) {
                ordenesInspeccionFiltradas.add(ordenInspeccion);
            }
        });
        return ordenarOI(ordenesInspeccionFiltradas);
    }

    public List<OrdenInspeccion> ordenarOI(List<OrdenInspeccion> ordenesInspeccionToOrder) {
        return ordenesInspeccionToOrder.stream()
                .sorted((o1, o2) -> {
                    LocalDateTime f1 = o1.obtenerFechaFinalizacion();
                    LocalDateTime f2 = o2.obtenerFechaFinalizacion();
                    if (f1 == null && f2 == null) return 0;
                    if (f1 == null) return 1;
                    if (f2 == null) return -1;
                    return f1.compareTo(f2);
                })
                .collect(Collectors.toList());
    }

    // --- SETTERS DESDE PANTALLA (Ya no son @Override) ---

    public void tomarNumeroOI(Long selectedOrdenNumero) {
        selectedOrden = ordenesInspeccionFiltradas.stream()
                .filter(oi -> oi.getNumeroOrden().equals(selectedOrdenNumero))
                .findFirst().orElse(null);
    }

    public void tomarDatosObservacion(String observacion) {
        this.observaciones = observacion;
    }

    public void tomarMFSyComentario(TipoMotivo motivoSeleccionado, String comentario) {
        this.motivosFueraServicioSelection.add(new MotivoFueraServicio(comentario, motivoSeleccionado));
    }

    public void tomarConfirmacioncierreOI(Boolean input) {
        if (selectedOrden != null) {
            this.confirmacionCierre = input;
        }
    }

    // --- BÚSQUEDAS INTERNAS ---

    private void buscarEstadoFS() {
        estados.stream()
                .filter(e -> e.esAmbitoSismografo() && e.esFueraDeServicio())
                .findFirst().ifPresent(this::setEstadoFS);
    }

    private void buscarEstadoCerradoOI() {
        estados.stream()
                .filter(e -> e.esAmbitoOrdendeInspeccion() && e.esCerrada())
                .findFirst().ifPresent(this::setEstadoCerrada);
    }
}