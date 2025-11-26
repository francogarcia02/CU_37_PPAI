package control;

import boundary.InterfazCCRS;
import boundary.InterfazMail;
import control.notificacion.DatosNotificacionCierre;
import control.notificacion.IObservadorCierreOrden;
import control.notificacion.ISujetoCierreOrden;
import control.persistencia.OrdenDAO;
import entity.*;
import interfaces.GestorOrdenInterface;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class GestorOrden implements GestorOrdenInterface, ISujetoCierreOrden {

    // --- Atributos de Negocio y Estado ---
    private Usuario usuarioLogueado;
    private Empleado RI;
    private OrdenInspeccion selectedOrden;
    private String selectedDecicionSismografo;
    private String observaciones;

    // Listas de referencia
    private List<Empleado> empleados;
    private List<TipoMotivo> tiposMotivos;
    private List<MotivoFueraServicio> motivosFueraServicioSelection = new ArrayList<>();
    private List<Estado> estados;

    // Estados específicos buscados
    private Estado EstadoFS;
    private Estado EstadoCerrada;

    // Variables de control
    private Boolean confirmacionCierre;
    private List<OrdenInspeccion> ordenesInspeccionFiltradas = new ArrayList<>();
    private Sesion sesion;
    private OrdenDAO ordenDAO; // Persistencia

    // --- PATRÓN OBSERVER: Estructura ---
    private List<IObservadorCierreOrden> observadores = new ArrayList<>();

    // --- CONSTRUCTOR ---
    public GestorOrden(List<Empleado> empleados, List<TipoMotivo> tiposMotivos, List<Estado> estados, Sesion sesion, OrdenDAO ordenDAO) {
        this.empleados = empleados;
        this.tiposMotivos = tiposMotivos;
        this.estados = estados;
        this.sesion = sesion;
        this.ordenDAO = ordenDAO;
        // NOTA: Ya no instanciamos los observadores aquí.
        // Se instancian bajo demanda en notificarObservadores() según el flujo de la cátedra.
    }

    // --- MÉTODOS DEL PATRÓN OBSERVER (Implementación ISujeto) ---

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
        // El Loop del Patrón: Recorre y avisa
        for (IObservadorCierreOrden obs : observadores) {
            obs.actualizar(datos, evento);
        }
    }

    // ==========================================================================
    // === LÓGICA DE NEGOCIO PRINCIPAL (CU: Cerrar Orden de Inspección) ===
    // ==========================================================================

    public boolean cerrarOrdenSeleccionada() {
        // 1. Validaciones iniciales
        if (getConfirmacionCierre() && getObservaciones() != null) {

            System.out.printf("INFO [GestorOrden] Inicia proceso de cierre para Orden [%d]%n", getSelectedOrden().getNumeroOrden());

            // 2. Buscar estados necesarios
            buscarEstadoFS();
            buscarEstadoCerradoOI();

            if (getEstadoCerrada() == null) {
                System.err.println("ERROR: No se pudo encontrar el estado 'Cerrada'.");
                return false;
            }

            // 3. Delegar el cambio de estado a la Entidad (Experto)
            boolean cierreExitoso = getSelectedOrden().cerrar(
                    getObservaciones(),
                    getMotivosFueraServicioSelection(),
                    getEstadoCerrada(),
                    getRI());

            if (cierreExitoso) {
                // 4. Lógica del Sismógrafo
                if (!getMotivosFueraServicioSelection().isEmpty()) {
                    // Si hay motivos, se pone fuera de servicio
                    getSelectedOrden().enviarSismografoAReparar(getEstadoFS());
                }
                // Nota: El estado actual del sismógrafo lo consultaremos luego para el DTO.

                // 5. Persistencia (Actualizar en BD)
                ordenDAO.update(getSelectedOrden());

                // --- COMIENZO LÓGICA DE NOTIFICACIÓN (SoC) ---

                // PASO A: Generar los datos necesarios (DTO)
                DatosNotificacionCierre dto = this.generarDatosNotificacion();

                // PASO B: Configurar y disparar el patrón Observer
                this.notificarObservadores(dto);

                return true;
            }
        }
        return false;
    }

    /**
     * Metodo encargado EXCLUSIVAMENTE de recolectar la información y crear el DTO.
     * Aplica Alta Cohesión.
     */
    private DatosNotificacionCierre generarDatosNotificacion() {
        // 1. Obtener datos de la Orden (Respetando Ley de Demeter con métodos delegados)
        String idSismografo = getSelectedOrden().getIdSismografo();
        String nombreEstacion = getSelectedOrden().getNombreEstacion();
        String nombreResponsable = getRI().getNombreEmpleado();

        // Obtenemos el estado final del sismógrafo post-cierre
        String estadoSismoActual = getSelectedOrden().getNombreEstadoSismografo();

        // 2. Buscar emails de destinatarios (Lógica de negocio del Gestor)
        List<String> listaMails = obtenerMailsResponsablesReparacion();

        // 3. Convertir motivos (Objetos) a Strings primitivos para el DTO
        List<String> listaMotivosTexto = getMotivosFueraServicioSelection().stream()
                .map(m -> String.format("%s (%s)", m.getTipoMotivo().getDescripcion(),
                        (m.getComentario() != null ? m.getComentario() : "-")))
                .collect(Collectors.toList());

        // 4. Crear y retornar el DTO
        return new DatosNotificacionCierre(
                listaMails,                 // Emails
                idSismografo,               // ID Sismógrafo
                estadoSismoActual,          // Estado Nuevo
                LocalDateTime.now(),        // Fecha Hora
                listaMotivosTexto,          // Lista de Strings (Motivos)
                getSelectedOrden().getNumeroOrden(), // Nro Orden
                nombreEstacion,             // Estación
                nombreResponsable           // Responsable
        );
    }

    /**
     * Metodo encargado EXCLUSIVAMENTE de configurar los observadores y disparar la notificación.
     */
    private void notificarObservadores(DatosNotificacionCierre dto) {
        System.out.println("INFO [GestorOrden] Configurando observadores...");

        // 1. Limpiar observadores previos (si los hubiera)
        this.observadores.clear();

        // 2. Crear las instancias de los observadores concretos (Creator)
        IObservadorCierreOrden observadorMail = new InterfazMail();
        IObservadorCierreOrden observadorCCRS = new InterfazCCRS();

        // 3. Suscribirlos (Auto-suscripción)
        this.agregarObservador(observadorMail);
        this.agregarObservador(observadorCCRS);

        // 4. Disparar la notificación a todos los suscritos
        System.out.println("INFO [GestorOrden] Disparando evento 'CIERRE_ORDEN'...");
        this.notificar(dto, "CIERRE_ORDEN");
    }

    // ==========================================================================
    //                      === OTROS MÉTODOS DE APOYO ===
    // ==========================================================================

    @Override
    public List<String> obtenerMailsResponsablesReparacion() {
        return empleados.stream()
                .filter(Empleado::esResponsableReparaciones)
                .map(Empleado::obtenerMail)
                .collect(Collectors.toList());
    }

    @Override
    public Empleado buscarEmpleado() {
        usuarioLogueado = obtenerUsuarioLogueado();
        RI = usuarioLogueado.getEmpleado();
        return RI;
    }

    @Override
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
    public Usuario obtenerUsuarioLogueado() {
        return sesion.getUsuario();
    }

    // --- Métodos de interacción con UI ---
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
    public void tomarMFSyComentario(TipoMotivo motivoSeleccionado, String comentario) {
        this.getMotivosFueraServicioSelection().add(new MotivoFueraServicio(comentario, motivoSeleccionado));
    }

    @Override
    public void tomarConfirmacioncierreOI(Boolean input) {
        if(selectedOrden != null){
            setConfirmacionCierre(input);
        }
    }

    // --- Búsquedas internas ---
    @Override
    public void buscarEstadoFS() {
        estados.stream()
                .filter(e -> e.esAmbitoSismografo() && e.esFueraDeServicio())
                .findFirst()
                .ifPresent(this::setEstadoFS);
    }

    @Override
    public void buscarEstadoCerradoOI() {
        estados.stream()
                .filter(e -> e.esAmbitoOrdendeInspeccion() && e.esCerrada())
                .findFirst()
                .ifPresent(this::setEstadoCerrada);
    }

    // --- Métodos Legacy mantenidos por la interfaz ---
    @Override public List<String> stringificarMFS() { return new ArrayList<>(); }
    @Override public String stringificarOI(OrdenInspeccion o) { return ""; }
    @Override public void RecibirSelectedOption(String s) {}
    @Override public void manageSismografoFS() {}
    @Override public Boolean validarMotivo() { return true; }
    @Override public LocalDateTime getFechaHoraActual() { return LocalDateTime.now(); }
    @Override public void FinCU() {}
    @Override public void RecibirTipoMotivos(List<TipoMotivo> l) { setTiposMotivos(l); }
    @Override public void pasarToPantallaOIs() {}
}