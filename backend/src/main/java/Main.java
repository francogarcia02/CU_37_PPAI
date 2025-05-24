import boundary.PantallaOrden;
import control.GestorOrden;
import control.MOCKDATAGenerator;
import entity.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {
    // generación de datos de prueba
    MOCKDATAGenerator mockDataGenerator = new MOCKDATAGenerator();

    Empleado empleadoRI = mockDataGenerator.generarEmpleado(1);
    Empleado otroEmpleadoNoRIParaPruebas = mockDataGenerator.generarEmpleado(2);
    Sesion sesion = mockDataGenerator.generarSesion(empleadoRI.getUsuario());
    EstacionSismologica estacionSismologica = mockDataGenerator.generarEstacionSismologica();
    List<Empleado> empleados = List.of(empleadoRI, otroEmpleadoNoRIParaPruebas); // para pruebas>
    List<Estado> estados = mockDataGenerator.generarEstados();
    List<OrdenInspeccion> ordenesInspeccion = mockDataGenerator.generarOrdenesInspeccion(empleadoRI, estacionSismologica,estados);
    List<TipoMotivo> listaMotivos = mockDataGenerator.generarTipoMotivo();

    GestorOrden gestorOrden = new GestorOrden();
    gestorOrden.RecibirSesion(sesion);
    gestorOrden.RecibirOrdenesInspeccion(ordenesInspeccion);
    gestorOrden.RecibirEmpleados(empleados);
    gestorOrden.RecibirTipoMotivos(listaMotivos);
    gestorOrden.RecibirEstados(estados);


    PantallaOrden pantallaOrden = new PantallaOrden(gestorOrden);
    gestorOrden.RecibirPantallaOrden(pantallaOrden);

    pantallaOrden.mainProcess();





    }
}
