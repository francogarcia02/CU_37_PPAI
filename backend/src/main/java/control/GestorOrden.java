package control;

import boundary.PantallaOrden;
import entity.Empleado;
import entity.OrdenInspeccion;
import entity.Sesion;
import entity.Usuario;
import lombok.Data;

import java.util.List;

@Data
public class GestorOrden {

    private String selectedOption;
    private PantallaOrden pantallaOrden;
    private List<OrdenInspeccion> ordenesInspeccion;
    private Sesion sesion;
    private List<Empleado> empleados;
    private OrdenInspeccion selectedOrden;

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
        if (selectedOption.equals("1")) {
            System.out.println();
            // busca el empleado asociado a la sesion
            Empleado RI = mappearEmpleadoPorUsuario(sesion.getUsuario());

            System.out.println("Listado de Ordenes Inspeccion para cerrar: ");


            ordenesInspeccion.forEach(ordenInspeccion -> {

                if (
                        ordenInspeccion.sosFinalizada()
                                && ordenInspeccion.esTuRI(RI)) {

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

            if (selectedOrden != null) {
                // Proceed with the selected order
                System.out.println("Se encontró una orden con el número: " + selectedOrdenNumero);
                String observaciones = pantallaOrden.solicitarObservaciones();
                while (observaciones == "") {
                    System.out.println("Es obligatorio ingresar observaciones para cerrar la orden de inspeccion");
                    observaciones = pantallaOrden.solicitarObservaciones();
                }
                System.out.println("~GestorOrdenes~ He recibido las observaciones: " + observaciones);

            } else {
                System.out.println("No se encontró ninguna orden con el número ingresado.");
            }
        }
    }

}

