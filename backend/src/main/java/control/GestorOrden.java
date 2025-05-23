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

    public void RecibirPantallaOrden(PantallaOrden pantallaOrden) {
        this.pantallaOrden = pantallaOrden;
        System.out.print("~GestorOrdenes~ He recibido la pantalla correctamente");
        System.out.println();
    }

    public void RecibirOrdenesInspeccion(List<OrdenInspeccion> ordenesInspeccion) {
        this.ordenesInspeccion = ordenesInspeccion;
        System.out.print("~GestorOrdenes~ He recibido las Ordenes de Inspeccion correctamente");
        System.out.println();
    }

    public void RecibirEstados(List<Estado> estados) {
        this.estados = estados;
        System.out.print("~GestorOrdenes~ He recibido los estados correctamente");
        System.out.println();
    }

    public void RecibirTipoMotivos(List<TipoMotivo> tipoMotivos) {
        this.tiposMotivos = tipoMotivos;
        System.out.print("~GestorOrdenes~ He recibido los tipos de motivo correctamente");
        System.out.println();
    }

    public void RecibirSesion(Sesion sesion) {
        this.sesion = sesion;
        System.out.print("~GestorOrdenes~ He recibido la Sesion correctamente");
        System.out.println();
    }

    public void RecibirEmpleados(List<Empleado> empleados) {
        this.empleados = empleados;
        System.out.print("~GestorOrdenes~ He recibido los Empleados correctamente");
        System.out.println();
    }

    public void RecibirselectedOption(String selectedOption) {
        this.selectedOption = selectedOption;
        System.out.print("~GestorOrdenes~ He recibido la opción seleccionada correctamente");

        System.out.println();
        mainProcess();
    }

    private Empleado mappearEmpleadoPorUsuario(Usuario usuario) {
        for (Empleado empleado : empleados) {
            if (empleado.getUsuario().equals(usuario)) {
                return empleado;
            }
        }
        return null;
    }


    private void mainProcess() {
        while (!selectedOption.equals("0")) {
            if (selectedOption.equals("1")) {
                System.out.println();
                // busca el empleado asociado a la sesion
                Empleado RI = mappearEmpleadoPorUsuario(sesion.getUsuario());

                System.out.println("Listado de Ordenes Inspeccion para cerrar: ");

                ordenesInspeccion.forEach(ordenInspeccion -> {
                    Boolean condition1 = ordenInspeccion.sosFinalizada();
                    Boolean condition2 = ordenInspeccion.esTuRI(RI);
                    if (condition1 && condition2) {
                        String ordenInspeccionString = ordenInspeccion.toStringForPantalla();
                        pantallaOrden.mostrarOrdenInspeccion(ordenInspeccionString);
                    }
                });

                System.out.println("ingrese el numero de la Orden de Inspeccion a cerrar ");
                String selectedOrdenNumero = pantallaOrden.leerEntradaUsuario();
                selectedOrdenNumero = selectedOrdenNumero.trim();
                selectedOrden = null;

                for (OrdenInspeccion ordenInspeccion : ordenesInspeccion) {
                    if (ordenInspeccion.getNumeroOrden().equals(Long.parseLong(selectedOrdenNumero))) {
                        selectedOrden = ordenInspeccion;
                        break; // Exit the loop once we find the order
                    }
                }

                String observaciones = null;
                if (selectedOrden != null) {
                    // Proceed with the selected order
                    System.out.println("Se encontró una orden con el número: " + selectedOrdenNumero);
                    observaciones = pantallaOrden.solicitarObservaciones();
                    while (observaciones == "") {
                        System.out.println("Es obligatorio ingresar observaciones para cerrar la orden de inspeccion");
                        observaciones = pantallaOrden.solicitarObservaciones();
                    }
                    System.out.println("~GestorOrdenes~ He recibido las observaciones: " + observaciones);

                } else {
                    System.out.println("No se encontró ninguna orden con el número ingresado.");
                }

                String selectedDecicion = pantallaOrden.confirmarActualizacionSituacion();
                while (!selectedDecicion.equals("1") && !selectedDecicion.equals("0")) {
                    selectedDecicion = pantallaOrden.confirmarActualizacionSituacion();
                }

                sismografoSelected = selectedOrden.getEstacionSismologica().getSismografo();
                List<MotivoFueraServicio> motivosFueraServicio = new ArrayList<>();

                if (selectedDecicion.equals("1")) {
                    String motSelected = "";
                    while (!motSelected.equals("000")) {
                        System.out.println();
                        System.out.println("Tipos de motivo: ");
                        for (int i = 0; i < tiposMotivos.size(); i++) {
                            TipoMotivo tipoMotivo = tiposMotivos.get(i);
                            String descripcionConIndice = i + ": " + tipoMotivo.getDescripcion();
                            pantallaOrden.mostrarTipoMotivosFueraServicio(descripcionConIndice);
                        }

                        motSelected = pantallaOrden.SolicitarMFS();

                        if (motSelected.equals("000")) {
                            break;
                        }

                        try {
                            int index = Integer.parseInt(motSelected);
                            if (index >= 0 && index < tiposMotivos.size()) {
                                TipoMotivo motivoSeleccionado = tiposMotivos.get(index);
                                System.out.println("Seleccionaste: " + motivoSeleccionado.getDescripcion());
                                String comentario = pantallaOrden.solicitarMotivoComentario();
                                motivosFueraServicio.add(new MotivoFueraServicio(comentario, motivoSeleccionado));
                            } else {
                                System.out.println("Número fuera de rango.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Entrada inválida. Ingrese un número o '000' para salir.");
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

