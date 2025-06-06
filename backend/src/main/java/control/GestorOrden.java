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
    private List<Estado> estados;

    private List<OrdenInspeccion> ordenesInspeccionFiltradas = new ArrayList<>();
    private List<String> mailsResponsablesReparaciones = new ArrayList<>();
    private PantallaOrden pantallaOrden;
    private Sesion sesion;

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

    public void RecibirTipoMotivos(List<TipoMotivo> tipoMotivos) {
        this.tiposMotivos = tipoMotivos;
        pantallaOrden.comunicarFeedbackGestor("He recibido los tipos de motivo correctamente");
    }

    public void RecibirSesion(Sesion sesion) {
        this.sesion = sesion;
        pantallaOrden.comunicarFeedbackGestor("He recibido la Sesion correctamente");
    }

    public void RecibirEmpleados(List<Empleado> empleados) {
        this.empleados = empleados;
        pantallaOrden.comunicarFeedbackGestor("He recibido los Empleados correctamente");
    }

    public void RecibirselectedOption(String selectedOption) {
        this.selectedOption = selectedOption;
        pantallaOrden.comunicarFeedbackGestor("He recibido la opción seleccionada correctamente");
        mainProcess();
    }


    @Override
    public void registrarCierre() {
    selectedOption = "1";
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
            Boolean condition1 = ordenInspeccion.estaRealizada();
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
    public List<MotivoFueraServicio> buscarMFS() {
        return List.of();
    }

    @Override
    public void tomarMFS() {

    }

    @Override
    public void tomarComentario() {

    }

    @Override
    public void buscarEstadosFS() {

    }

    @Override
    public Boolean validarMotivo() {
        return null;
    }

    @Override
    public Estado buscarEstadoOrdenInspección(OrdenInspeccion ordenInspeccion) {
        return null;
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
                + "Estado actual del sismografo: " + selectedOrden.getEstacionSismologica().getSismografo().getEstado().getNombre() + "\n"
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


    // este metodo privado se encarga de llevar el proceso de cierre
    // se llama mainProcess porque es el unico proceso que hay en el sistema,
    // podría llamarse cierreOIProcess si fuera necesario
    private void mainProcess() {
        while (!selectedOption.equals("2")) {
            if (selectedOption.equals("1")) {
                // busca el empleado asociado a la sesion

                this.buscarEmpleado();
                pantallaOrden.comunicarFeedbackGestorLeve("Listado de Ordenes Inspeccion para cerrar: ");

                this.buscarOrdenesInspeccion();

                this.pasarToPantallaOIs();

                this.tomarNumeroOI(
                        pantallaOrden.tomarNumeroOI()
                );


                if (this.selectedOrden != null) {
                    this.tomarDatosObservacion(
                    pantallaOrden.solicitarObservacion()
                    );
                } else {
                    pantallaOrden.comunicarFeedbackGestor("No se encontró ninguna orden con el número ingresado.");
                    pantallaOrden.comunicarFeedbackGestor("será redirigido al menu principal");
                    pantallaOrden.mostrarOpciones();
                }

                this.tomarSeleccionDecicionSismografo(
                        pantallaOrden.confirmarActualizacionSituacionSismografo()
                        );



                List<MotivoFueraServicio> motivosFueraServicio = new ArrayList<>();

                // selectedDecicion refiere a la decisión del usuario respecto a actualizar la situación del sismografo
                if (selectedDecicionSismografo.equals("1")) {
                    String motSelected = "";
                    while (!motSelected.equals("0")) {
                        pantallaOrden.comunicarFeedbackGestorLeve("Tipos de motivo: ");
                        for (int i = 0; i < tiposMotivos.size(); i++) {
                            TipoMotivo tipoMotivo = tiposMotivos.get(i);
                            String descripcionConIndice = (i+1) + ": " + tipoMotivo.getDescripcion();
                            pantallaOrden.mostrarTipoMotivosFueraServicio(descripcionConIndice);
                        }

                        motSelected = String.valueOf(pantallaOrden.SolicitarMFS());
                        System.out.println("usted selecciono como motSelected: " + motSelected);

                        if (motSelected.equals("0")) {
                            break;
                        }

                        // Ajustar el índice (restar 1 porque mostramos desde 1)
                        int index = Integer.parseInt(motSelected) - 1;
                        System.out.println("el index es: " + index);

                        try {
                            if (index >= 0 && index < tiposMotivos.size()) {
                                TipoMotivo motivoSeleccionado = tiposMotivos.get(index);
                                pantallaOrden.comunicarFeedbackGestorLeve("Seleccionaste: " + motivoSeleccionado.getDescripcion());
                                String comentario = pantallaOrden.solicitarMotivoComentario();
                                motivosFueraServicio.add(new MotivoFueraServicio(comentario, motivoSeleccionado));
                                System.out.println(motivosFueraServicio);
                            } else {
                                pantallaOrden.comunicarFeedbackGestor("Número fuera de rango.");
                            }
                        } catch (NumberFormatException e) {
                            pantallaOrden.comunicarFeedbackGestor("Entrada inválida. Ingrese un número o '0' para salir.");
                        }

                        selectedOrden.enviarSismografoAReparar(estados.get(8));

                    }

                }

                Boolean solicitudConfirmacion = pantallaOrden.solicitarConfirmacionCierre();


                if (solicitudConfirmacion && observaciones != null && !motivosFueraServicio.isEmpty()) {
                    Boolean result = selectedOrden.cerrar(observaciones, motivosFueraServicio, estados.get(13), RI  );
                    pantallaOrden.mostrarResultadoCierre(result);

                    // notificar RRs via email Y publicar resultados en monitores CCRS
                    // : La notificación debe incluir la identificación del sismógrafo, el nombre del estado Fuera de Servicio, la fecha y
                    //hora de registro del nuevo estado, y los motivos y comentarios asociados al cambio.
                    String mensaje = confeccionarMensaje(selectedOrden);
                    enviarNotificacionMail(mensaje);

                } else if (solicitudConfirmacion && observaciones != null){
                    Boolean result = selectedOrden.cerrar(observaciones, motivosFueraServicio, estados.get(13), RI  );
                    pantallaOrden.mostrarResultadoCierre(result);

                    // notificar RRs via email Y publicar resultados en monitores CCRS
                    // : La notificación debe incluir la identificación del sismógrafo, el nombre del estado Fuera de Servicio, la fecha y
                    //hora de registro del nuevo estado, y los motivos y comentarios asociados al cambio.
                    String mensaje = confeccionarMensaje(selectedOrden);
                    enviarNotificacionMail(mensaje);
                };



            }
            pantallaOrden.imprimirOndasSismicas("usted está siendo redirigido al menú principal");

            // al finalizar el proceso limpiamos la seleccion
            selectedOption = "";
            pantallaOrden.mostrarOpciones();
        }
    }
}

