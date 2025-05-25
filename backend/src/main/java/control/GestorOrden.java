package control;

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


    // este metodo privado se encarga de llevar el proceso de cierre
    // se llama mainProcess porque es el unico proceso que hay en el sistema,
    // podría llamarse cierreOIProcess si fuera necesario
    private void mainProcess() {
        while (!selectedOption.equals("2")) {
            if (selectedOption.equals("1")) {
                // busca el empleado asociado a la sesion
                Empleado RI = mappearEmpleadoPorUsuario(sesion.getUsuario());
                pantallaOrden.comunicarFeedbackGestorLeve("Listado de Ordenes Inspeccion para cerrar: ");
                ordenesInspeccion.forEach(ordenInspeccion -> {
                    Boolean condition1 = ordenInspeccion.sosFinalizada();
                    Boolean condition2 = ordenInspeccion.esTuRI(RI);
                    if (condition1 && condition2) {
                        ordenesInspeccionFiltradas.add(ordenInspeccion);
                        String ordenInspeccionString = ordenInspeccion.toStringForPantalla();
                        pantallaOrden.mostrarOrdenInspeccion(ordenInspeccionString);
                    }
                });


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
                }

                String selectedDecicion = String.valueOf(pantallaOrden.confirmarActualizacionSituacion());
                while (!selectedDecicion.equals("1") && !selectedDecicion.equals("0")) {
                    selectedDecicion = String.valueOf(pantallaOrden.confirmarActualizacionSituacion());
                }

                sismografoSelected = selectedOrden.getEstacionSismologica().getSismografo();
                List<MotivoFueraServicio> motivosFueraServicio = new ArrayList<>();

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

                        if (motSelected.equals("0")) {
                            break;
                        }

                        // Ajustar el índice (restar 1 porque mostramos desde 1)
                        int index = Integer.parseInt(motSelected) - 1;

                        try {

                            if (index >= 0 && index < tiposMotivos.size()) {
                                TipoMotivo motivoSeleccionado = tiposMotivos.get(index);
                                pantallaOrden.comunicarFeedbackGestorLeve("Seleccionaste: " + motivoSeleccionado.getDescripcion());
                                String comentario = pantallaOrden.solicitarMotivoComentario();
                                motivosFueraServicio.add(new MotivoFueraServicio(comentario, motivoSeleccionado));
                            } else {
                                pantallaOrden.comunicarFeedbackGestor("Número fuera de rango.");
                            }
                        } catch (NumberFormatException e) {
                            pantallaOrden.comunicarFeedbackGestor("Entrada inválida. Ingrese un número o '0' para salir.");
                        }
                    }

                }

                Boolean solicitudConfirmacion = pantallaOrden.solicitarConfirmacionCierre();
                List<Estado> estadosFS = new ArrayList<>();
                if (solicitudConfirmacion && observaciones != null && motivosFueraServicio.size() > 0) {
                    estados.forEach(estado -> {
                        String ambito = estado.getAmbito();
                        String nombre = estado.getNombre();
                        if (ambito.equals("SISMOGRAFO") && nombre.equals("fueraServicio")) {
                            estadosFS.add(estado);
                        }
                    });
                    Boolean result = selectedOrden.cerrar(observaciones, motivosFueraServicio, estados.get(13), RI  );
                    pantallaOrden.mostrarResultadoCierre(result);
                } else if (solicitudConfirmacion && observaciones != null){
                    Boolean result = selectedOrden.cerrar(observaciones, motivosFueraServicio, estados.get(13), RI  );
                    pantallaOrden.mostrarResultadoCierre(result);
                };
            }
            pantallaOrden.imprimirOndasSismicas("usted está siendo redirigido al menú principal");
            pantallaOrden.mostrarOpciones();
        }
    }

}

