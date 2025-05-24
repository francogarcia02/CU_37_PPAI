import boundary.PantallaOrden;
import control.GestorOrden;
import control.MOCKDATAGenerator;
import entity.*;

import java.util.List;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
    // generación de datos de prueba
    MOCKDATAGenerator mockDataGenerator = new MOCKDATAGenerator();

    Empleado empleadoRI = mockDataGenerator.generarEmpleado(1);
    Empleado otroEmpleadoNoRIParaPruebas = mockDataGenerator.generarEmpleado(2);
    Sesion sesion = mockDataGenerator.generarSesion(empleadoRI.getUsuario());
    EstacionSismologica estacionSismologica1 = mockDataGenerator.generarEstacionSismologica(1);
    EstacionSismologica estacionSismologica2 = mockDataGenerator.generarEstacionSismologica(2);
    List<Empleado> empleados = List.of(empleadoRI, otroEmpleadoNoRIParaPruebas); // para pruebas>
    List<Estado> estados = mockDataGenerator.generarEstados();
    List<OrdenInspeccion> ordenesInspeccion = new ArrayList<>(mockDataGenerator.generarOrdenesInspeccion(empleadoRI, estacionSismologica1, estados));
    ordenesInspeccion.addAll(mockDataGenerator.generarOrdenesInspeccion(empleadoRI, estacionSismologica2, estados));
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
