package control;

import boundary.InterfazMail;
import boundary.PantallaOrden;
import entity.CambioEstado;
import entity.Empleado;
import entity.OrdenInspeccion;
import entity.Sesion;
import entity.Usuario;
import entity.TipoMotivo;
import entity.Estado;
import entity.Sismografo;
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

    private String selectedOption;
    private String selectedDecicionSismografo;
    private String observaciones;
    private List<Empleado> empleados;
    private List<TipoMotivo> tiposMotivos;
    private List<MotivoFueraServicio> motivosFueraServicioSelection = new ArrayList<>();
    public int selectedTempMFSIndex;
    public String inputTempComentrarioFS;
    private List<Estado> estados;
    private Estado EstadoFS;
    private Estado EstadoCerrada;
    private Boolean confirmacionCierre;
    private List<OrdenInspeccion> ordenesInspeccionFiltradas = new ArrayList<>();
    private List<String> mailsResponsablesReparaciones = new ArrayList<>();
    private PantallaOrden pantallaOrden;
    private Sesion sesion;
    private Empleado empleadoLogueado;
    private List<TipoMotivo> motivosFueraServicio;

    public GestorOrden(List<OrdenInspeccion> ordenesInspeccion, List<Empleado> empleados, List<TipoMotivo> tiposMotivos, List<Estado> estados, Sesion sesion) {
        this.ordenesInspeccion = ordenesInspeccion;
        this.empleados = empleados;
        this.tiposMotivos = tiposMotivos;
        this.estados = estados;
        this.sesion = sesion;
    }

    // Todos estos metodos "Recibir" son para que el gestor conozca las clases necesarias

    public void RecibirPantallaOrden(PantallaOrden pantallaOrden) {
        this.pantallaOrden = pantallaOrden;
        pantallaOrden.comunicarFeedbackGestor("He recibido la pantalla correctamente");
    }

    public void RecibirOrdenesInspeccion(List<OrdenInspeccion> ordenesInspeccion) {
        this.ordenesInspeccion = ordenesInspeccion;
        pantallaOrden.comunicarFeedbackGestor("He recibido las Ordenes de Inspeccion correctamente");
    }

    public void RecibirEstados(List<Estado> estados) {
        this.estados = estados;
        pantallaOrden.comunicarFeedbackGestor("He recibido los estados correctamente");
    }


    public void RecibirSesion(Sesion sesion) {
        this.sesion = sesion;
        this.empleadoLogueado = sesion.getUsuario().getEmpleado();
        pantallaOrden.comunicarFeedbackGestor("He recibido la Sesion correctamente");
    }

    public void RecibirEmpleados(List<Empleado> empleados) {
        this.empleados = empleados;
        pantallaOrden.comunicarFeedbackGestor("He recibido los Empleados correctamente");
    }

    public void RecibirSelectedOption(String selectedOption) {
        this.selectedOption = selectedOption;
        pantallaOrden.comunicarFeedbackGestor("He recibido la opción seleccionada correctamente");
    }

    @Override
    public Empleado buscarEmpleado() {
        usuarioLogueado = obtenerUsuarioLogueado();
        RI = usuarioLogueado.getEmpleado();
        return RI;
    }

    @Override
    public List<OrdenInspeccion> buscarOrdenesInspeccion() {
        // limpia la lista de ordenesInspeccionFiltradas, este paso es importante porque sino
        // el sistema cree que hay ordenes cuando no las hay
        ordenesInspeccionFiltradas.clear();

        // para cada ordenInspeccion en la lista de ordenesInspeccion, se verifica si es finalizada
        // y si está asignada al RI, y si es asi, se agrega a la lista de
        // ordenesInspeccionFiltradas y se muestra en la pantalla
        ordenesInspeccion.forEach(ordenInspeccion -> {
            Boolean condition1 = ordenInspeccion.estaFinalizada();
            Boolean condition2 = ordenInspeccion.esTuRI(RI);

            if (condition1 && condition2) {
                ordenesInspeccionFiltradas.add(ordenInspeccion);
            }
        });

        if (ordenesInspeccionFiltradas.isEmpty()) {
            pantallaOrden.comunicarFeedbackGestor("No hay Ordenes de Inspeccion para cerrar");
            return ordenarOI(ordenesInspeccionFiltradas);
        } else {
            return ordenarOI(ordenesInspeccionFiltradas);
        }

    };

    @Override
    public List<OrdenInspeccion> ordenarOI(List<OrdenInspeccion> ordenesInspeccionToOrder) {
        return ordenesInspeccionToOrder.stream()
                .sorted((o1, o2) -> {
                    LocalDateTime fecha1 = o1.obtenerFechaFinalizacion();
                    LocalDateTime fecha2 = o2.obtenerFechaFinalizacion();

                    // Handle null cases
                    if (fecha1 == null && fecha2 == null) {
                        return 0;
                    }
                    if (fecha1 == null) {
                        return 1; // Put nulls at the end
                    }
                    if (fecha2 == null) {
                        return -1; // Put nulls at the end
                    }
                    return fecha1.compareTo(fecha2); // Ascending order
                })
                .collect(Collectors.toList());
    }

    @Override
    public String stringificarOI(OrdenInspeccion ordenesInspeccionToStringify) {
        return ordenesInspeccionToStringify.toStringForPantalla();
    }

    @Override
    public void pasarToPantallaOIs() {
        ordenesInspeccionFiltradas.forEach(ordenInspeccion -> {
            pantallaOrden.mostrarOI(
                    this.stringificarOI(ordenInspeccion)
            );
        });

    }

    @Override
    public void tomarNumeroOI(Long selectedOrdenNumero) {
        selectedOrden = null;

        for (OrdenInspeccion ordenInspeccion : ordenesInspeccion) {
            // se aplica patron experto para comparar el nro de la orden de inspeccion ingresado
            Boolean comparedOI = ordenInspeccion.compareNroOrder(selectedOrdenNumero);
            if (comparedOI) {
                selectedOrden = ordenInspeccion;

                if (!ordenesInspeccionFiltradas.contains(selectedOrden)) {
                    pantallaOrden.comunicarFeedbackGestor("Ingresó una orden de inspeccion que no le pertenece o no se encuentra en estado finalizada");
                    pantallaOrden.comunicarFeedbackGestor("será redirigido al menu principal");
                    pantallaOrden.mostrarOpciones();
                }
            }

        }

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
    public void manageSismografoFS() {

        while (!pantallaOrden.getSelectedMFS().equals("0")) {
            pantallaOrden.comunicarFeedbackGestorLeve("Tipos de motivo: ");

            pantallaOrden.mostrarMFS(
                    this.stringificarMFS()
            );

            pantallaOrden.tomarMFS(
                    pantallaOrden.SolicitarMFS()
            );

            if (pantallaOrden.getSelectedMFS().equals("0")) {
                break;
            }

            // Ajustar el índice (restar 1 porque mostramos desde 1)
            this.setSelectedTempMFSIndex(
                    Integer.parseInt(pantallaOrden.getSelectedMFS()) - 1
            );

            if (validarMotivo()) {
                TipoMotivo motivoSeleccionado = this.getTiposMotivos().get(selectedTempMFSIndex);
                motivosFueraServicio.add(motivoSeleccionado);
                pantallaOrden.comunicarFeedbackGestorLeve("Seleccionaste: " + motivoSeleccionado.getDescripcion());
                pantallaOrden.tomarComentario(
                        pantallaOrden.solicitarMotivoComentario()
                );
                this.tomarMFSyComentario(motivoSeleccionado,pantallaOrden.getInputComentrarioFS());
            } else {
                pantallaOrden.comunicarFeedbackGestor("Número fuera de rango.");
            }
        }
    }


    @Override
    public List<String> stringificarMFS() {
        List<String> stringifiedMFS = new ArrayList<>();

        for (int i = 0; i < this.getTiposMotivos().size(); i++) {
            TipoMotivo tipoMotivo = this.getTiposMotivos().get(i);
            String descripcionConIndice = (i+1) + ": " + tipoMotivo.getDescripcion();
            stringifiedMFS.add(descripcionConIndice);
        }
        return stringifiedMFS;
    }

    @Override
    public void tomarMFSyComentario(TipoMotivo motivoSeleccionado, String comentario) {
        this.getMotivosFueraServicioSelection().add(new MotivoFueraServicio(comentario, motivoSeleccionado));

    }

    @Override
    public void tomarConfirmacioncierreOI(Boolean input) {
        setConfirmacionCierre(input);
    }


    @Override
    public void buscarEstadoFS() {
        estados.stream().forEach(estado -> {
            if (estado.esAmbitoSismografo() && estado.esFueraDeServicio()) {
                this.setEstadoFS(estado);
            }
        });

    }

    @Override
    public void  buscarEstadoCerradoOI() {
        estados.stream().forEach(estado -> {
            if (estado.esAmbitoOrdendeInspeccion() && estado.esCerrada()) {
                this.setEstadoCerrada(estado);
            }
        });

    }

    @Override
    public Boolean validarMotivo() {
        if (selectedTempMFSIndex >= 0 && selectedTempMFSIndex < this.getTiposMotivos().size()) {
            return true;
        } else {
            return false;
        }
    }



    @Override
    public LocalDateTime getFechaHoraActual() {
        return null;
    }


    @Override
    public List<String> obtenerMailsResponsablesReparacion(){
        List<Empleado> empleadosResponsablesReparaciones = new ArrayList<>();

        // empleados.stream(): Crea un stream (flujo de datos) a partir de la lista de empleados.
        // .filter(Empleado::esResponsableReparaciones): Filtra los empleados utilizando el predicado esResponsableReparaciones.
        // .forEach(empleadosResponsablesReparaciones::add): Por cada empleado filtrado, lo agrega a la colección empleadosResponsablesReparaciones usando una referencia de método.
        empleados.stream()
                .filter(Empleado::esResponsableReparaciones)
                .forEach(empleadosResponsablesReparaciones::add);

        List<String> mailsResponsablesReparaciones = new ArrayList<>();

        // empleadosResponsablesReparacion.stream(): Crea un stream (flujo de datos)  a partir de la lista de empleadosResponsablesReparacion.
        // .map(empleado -> empleado.getUsuario().getEmail()): Transforma cada objeto Empleado en su dirección de correo electrónico
        // .forEach(mailsResponsablesReparacion::add): Por cada email obtenido, lo agrega a la colección mailsResponsablesReparaciones usando una referencia de método.
        empleadosResponsablesReparaciones.stream()
                .map(empleado -> empleado.obtenerMail())
                .forEach(mailsResponsablesReparaciones::add);

        // finalmente, se devuelve la colección mailsResponsablesReparaciones
        return mailsResponsablesReparaciones;
    }

    @Override
    public void enviarNotificacionMail(String mensaje) {
        List<String> mailsResponsablesReparaciones = obtenerMailsResponsablesReparacion();

        InterfazMail interfazMail = new InterfazMail();

        mailsResponsablesReparaciones.stream().forEach(mail -> {
            System.out.println(interfazMail.enviarMail(mail, mensaje));
        });

    }

    @Override
    public void publicarMonitores() {

    }

    @Override
    public String confeccionarMensaje(OrdenInspeccion selectedOrden) {
        String mensaje = "";
        mensaje = "Estimado(a) responsable de reparaciones" + ",\n\n"
                + "La Orden de Inspeccion " + selectedOrden.getNumeroOrden() + " ha sido cerrada. \n\n"
                + "Detalles:\n"
                + "Estación Sismológica: " + selectedOrden.getEstacionSismologica().getNombreEstacion() + "\n"
                + "Responsable de la Orden: " + selectedOrden.getResponsableOrdenInspeccion().getNombreEmpleado() + "\n"
                + "ID sismografo: " + selectedOrden.getEstacionSismologica().getSismografo().getIdSismografo() + "\n\n"
                + "Estado actual del sismografo: " + selectedOrden.getEstacionSismologica().getSismografo().getEstadoActual().getNombre() + "\n"
                + "fecha y hora nuevo estado: " + selectedOrden.obtenerCambioEstadoActual().getFechaHorainicio() + "\n"
                + "Motivos:\n" + (

                (selectedOrden.obtenerCambioEstadoActual().getMotivosCambioEstados() != null) ?(
                        selectedOrden.obtenerCambioEstadoActual().getMotivosCambioEstados().stream()
                                .map(motivo ->
                                        "  - Motivo: " + motivo.getTipoMotivo().getDescripcion() + "\n"
                                                +
                                                "    Observaciones: " + (motivo.getComentario() != null ? motivo.getComentario() : "Sin observaciones") + "\n"
                                )
                                .collect(Collectors.joining("\n")))
                        : ("  No hay motivos registrados\n"
                ));

        return mensaje;
    }

    @Override
    public void FinCU() {

    }

    @Override
    public Usuario obtenerUsuarioLogueado() {
        usuarioLogueado = sesion.getUsuario();
        return usuarioLogueado;
    }

    @Override
    public void RecibirTipoMotivos(List<TipoMotivo> listaMotivos) {
        setTiposMotivos(listaMotivos);
    }
}

