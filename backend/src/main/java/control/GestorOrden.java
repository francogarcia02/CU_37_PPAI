package control;

import boundary.InterfazCCRS;
import boundary.InterfazMail;
import entity.Empleado;
import entity.OrdenInspeccion;
import entity.Sesion;
import entity.Usuario;
import entity.TipoMotivo;
import entity.Estado;
import entity.MotivoFueraServicio;
import interfaces.GestorOrdenInterface;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class GestorOrden implements GestorOrdenInterface {

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

    public GestorOrden(List<OrdenInspeccion> ordenesInspeccion, List<Empleado> empleados, List<TipoMotivo> tiposMotivos, List<Estado> estados, Sesion sesion) {
        this.ordenesInspeccion = ordenesInspeccion;
        this.empleados = empleados;
        this.tiposMotivos = tiposMotivos;
        this.estados = estados;
        this.sesion = sesion;
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

        ordenesInspeccion.forEach(ordenInspeccion -> {
            boolean condition1 = ordenInspeccion.estaFinalizada();
            boolean condition2 = ordenInspeccion.esTuRI(RI);

            if (condition1 && condition2) {
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
            buscarEstadoCerradoOI();

            boolean result = getSelectedOrden().cerrar(
                    getObservaciones(),
                    getMotivosFueraServicioSelection(),
                    getEstados().get(13), // Asumiendo que el estado 13 es "cierreDefinitivo"
                    getRI());

            if (!getMotivosFueraServicioSelection().isEmpty()) {
                getSelectedOrden().enviarSismografoAReparar(getEstadoFS());
                enviarNotificacionMail(confeccionarMensaje(getSelectedOrden()));
            }

            publicarMonitores();
            return result;
        }
        return false;
    }

    @Override
    public List<String> obtenerMailsResponsablesReparacion() {
        return empleados.stream()
                .filter(Empleado::esResponsableReparaciones)
                .map(Empleado::obtenerMail)
                .collect(Collectors.toList());
    }

    @Override
    public void enviarNotificacionMail(String mensaje) {
        List<String> mails = obtenerMailsResponsablesReparacion();
        InterfazMail interfazMail = new InterfazMail();
        mails.forEach(mail -> {
            System.out.println(interfazMail.enviarMail(mail, mensaje)); // Placeholder para el envío real
        });
    }

    @Override
    public void publicarMonitores() {
        InterfazCCRS interfazCCRS = new InterfazCCRS();
        interfazCCRS.imprimirMonitores();
        System.out.println("Publicación de monitores CCRS completada"); // Placeholder
    }

    @Override
    public String confeccionarMensaje(OrdenInspeccion orden) {
        String motivosStr = orden.obtenerCambioEstadoActual().getMotivosCambioEstados() != null ?
                orden.obtenerCambioEstadoActual().getMotivosCambioEstados().stream()
                        .map(motivo -> String.format("  - Motivo: %s\n    Observaciones: %s\n",
                                motivo.getTipoMotivo().getDescripcion(),
                                motivo.getComentario() != null ? motivo.getComentario() : "Sin observaciones"))
                        .collect(Collectors.joining("\n")) :
                "  No hay motivos registrados\n";

        return String.format(
                "Estimado(a) responsable de reparaciones,\n\n" +
                        "La Orden de Inspeccion %d ha sido cerrada.\n\n" +
                        "Detalles:\n" +
                        "Estación Sismológica: %s\n" +
                        "Responsable de la Orden: %s\n" +
                        "ID sismógrafo: %d\n\n" +
                        "Estado actual del sismógrafo: %s\n" +
                        "Fecha y hora nuevo estado: %s\n" +
                        "Motivos:\n%s",
                orden.getNumeroOrden(),
                orden.getEstacionSismologica().getNombreEstacion(),
                orden.getResponsableOrdenInspeccion().getNombreEmpleado(),
                orden.getEstacionSismologica().getSismografo().getIdSismografo(),
                orden.getEstacionSismologica().getSismografo().getEstadoActual().getNombre(),
                orden.obtenerCambioEstadoActual().getFechaHorainicio(),
                motivosStr
        );
    }

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