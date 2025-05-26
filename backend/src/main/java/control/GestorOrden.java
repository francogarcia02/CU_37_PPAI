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
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class GestorOrden {

    private String selectedOption;
    private PantallaOrden pantallaOrden;
    private List<OrdenInspeccion> ordenesInspeccion;
    private Sesion sesion;
    private List<Empleado> empleados;
    private OrdenInspeccion selectedOrden;
    private List<TipoMotivo> tiposMotivos;
    private Sismografo sismografoSelected;
    private List<Estado> estados;
    private List<OrdenInspeccion> ordenesInspeccionFiltradas = new ArrayList<>();
    private List<String> mailsResponsablesReparaciones = new ArrayList<>();

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

    // este metodo privado se encarga de mapear un usuario dado con un empleado
    private Empleado mappearEmpleadoPorUsuario(Usuario usuario) {
        for (Empleado empleado : empleados) {
            Boolean comparedEmployee = empleado.compareEmployee(usuario);
            if (comparedEmployee) {
                return empleado;
            }
        }
        return null;
    }

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
    };

    private String confeccionarMensajeMail(OrdenInspeccion selectedOrden) {
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

    public void enviarNotificacionMail(String mensaje) {
        List<String> mailsResponsablesReparaciones = obtenerMailsResponsablesReparacion();

        InterfazMail interfazMail = new InterfazMail();

        mailsResponsablesReparaciones.stream().forEach(mail -> {
            System.out.println(interfazMail.enviarMail(mail, mensaje));
        });
    }


    // este metodo privado se encarga de llevar el proceso de cierre
    // se llama mainProcess porque es el unico proceso que hay en el sistema,
    // podría llamarse cierreOIProcess si fuera necesario
    private void mainProcess() {
        while (!selectedOption.equals("2")) {
            if (selectedOption.equals("1")) {
                // busca el empleado asociado a la sesion
                Empleado RI = mappearEmpleadoPorUsuario(sesion.getUsuario());
                pantallaOrden.comunicarFeedbackGestorLeve("Listado de Ordenes Inspeccion para cerrar: ");

                // limpia la lista de ordenesInspeccionFiltradas, este paso es importante porque sino
                // el sistema cree que hay ordenes cuando no las hay
                ordenesInspeccionFiltradas.clear();

                // para cada ordenInspeccion en la lista de ordenesInspeccion, se verifica si es finalizada
                // y si está asignada al RI, y si es asi, se agrega a la lista de
                // ordenesInspeccionFiltradas y se muestra en la pantalla
                ordenesInspeccion.forEach(ordenInspeccion -> {
                    Boolean condition1 = ordenInspeccion.sosFinalizada();
                    Boolean condition2 = ordenInspeccion.esTuRI(RI);
                    if (condition1 && condition2) {
                        ordenesInspeccionFiltradas.add(ordenInspeccion);
                        String ordenInspeccionString = ordenInspeccion.toStringForPantalla();
                        pantallaOrden.mostrarOrdenInspeccion(ordenInspeccionString);
                    }
                });

                if (ordenesInspeccionFiltradas.isEmpty()) {
                    pantallaOrden.comunicarFeedbackGestor("No hay Ordenes de Inspeccion para cerrar");
                    pantallaOrden.mostrarOpciones();
                };

                // numericInputLong es un metodo de la pantalla que permite solamente ingresar un número
                // y este es retornado como un Long (wrapper class)
                Long selectedOrdenNumero = pantallaOrden.numericInputLong("ingrese el numero de la Orden de Inspeccion a cerrar ", "Solo puede ingresar números");
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

                String observaciones = null;
                if (selectedOrden != null) {
                    // Proceed with the selected order
                    pantallaOrden.comunicarFeedbackGestor("Se encontró una orden con el número: " + selectedOrdenNumero);
                    observaciones = pantallaOrden.solicitarObservaciones();
                    while (observaciones == "") {
                        pantallaOrden.comunicarFeedbackGestor("Es obligatorio ingresar observaciones para cerrar la orden de inspeccion");
                        observaciones = pantallaOrden.solicitarObservaciones();
                    }
                    pantallaOrden.comunicarFeedbackGestor("He recibido las observaciones: " + observaciones);
                } else {
                    pantallaOrden.comunicarFeedbackGestor("No se encontró ninguna orden con el número ingresado.");
                    pantallaOrden.comunicarFeedbackGestor("será redirigido al menu principal");
                    pantallaOrden.mostrarOpciones();
                }

                String selectedDecicion = String.valueOf(pantallaOrden.confirmarActualizacionSituacion());
                while (!selectedDecicion.equals("1") && !selectedDecicion.equals("0")) {
                    selectedDecicion = String.valueOf(pantallaOrden.confirmarActualizacionSituacion());
                }


                sismografoSelected = selectedOrden.getEstacionSismologica().getSismografo();
                List<MotivoFueraServicio> motivosFueraServicio = new ArrayList<>();

                // selectedDecicion refiere a la decisión del usuario respecto a actualizar la situación del sismografo
                if (selectedDecicion.equals("1")) {
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
                                System.out.println("entramos por true hasta el fondo, linea 250 gestor ");
                                System.out.println(motivosFueraServicio);
                            } else {
                                pantallaOrden.comunicarFeedbackGestor("Número fuera de rango.");
                            }
                        } catch (NumberFormatException e) {
                            pantallaOrden.comunicarFeedbackGestor("Entrada inválida. Ingrese un número o '0' para salir.");
                        }

                        sismografoSelected.ponerEnFueraServicio(estados.get(8));


                    }

                }

                Boolean solicitudConfirmacion = pantallaOrden.solicitarConfirmacionCierre();
                if (solicitudConfirmacion && observaciones != null && !motivosFueraServicio.isEmpty()) {
                    Boolean result = selectedOrden.cerrar(observaciones, motivosFueraServicio, estados.get(13), RI  );
                    pantallaOrden.mostrarResultadoCierre(result);
                } else if (solicitudConfirmacion && observaciones != null){
                    Boolean result = selectedOrden.cerrar(observaciones, motivosFueraServicio, estados.get(13), RI  );
                    pantallaOrden.mostrarResultadoCierre(result);
                };

                // notificar RRs via email Y publicar resultados en monitores CCRS
                // : La notificación debe incluir la identificación del sismógrafo, el nombre del estado Fuera de Servicio, la fecha y
                //hora de registro del nuevo estado, y los motivos y comentarios asociados al cambio.
                String mensaje = confeccionarMensajeMail(selectedOrden);
                enviarNotificacionMail(mensaje);

            }
            pantallaOrden.imprimirOndasSismicas("usted está siendo redirigido al menú principal");
            pantallaOrden.mostrarOpciones();
        }
    }



}

