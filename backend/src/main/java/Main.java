import boundary.PantallaOrden;
import control.GestorOrden;
import control.MOCKDATAGenerator;
import entity.*;

import java.util.List;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
    //  MOCKDATAGenerator se encarga de la generación de datos de prueba
    // como estamos programando solo un Caso de Uso necesitamos generar los datos que se generarian normalmente
    // en otros CUs como "iniciar sesion" o "cargar ordenes de inspeccion"
    MOCKDATAGenerator mockDataGenerator = new MOCKDATAGenerator();

    Empleado empleadoRI = mockDataGenerator.generarEmpleado(1);
    Usuario usuarioRI = mockDataGenerator.generarUsuario(1);
    Empleado otroEmpleadoNoRI = mockDataGenerator.generarEmpleado(2);
    List<Empleado> empleados = List.of(empleadoRI, otroEmpleadoNoRI);

    // generamos una sesion con el empleado que tiene rol RI, este paso es muy importante
    Sesion sesion = mockDataGenerator.generarSesion(usuarioRI);

    EstacionSismologica estacionSismologica1 = mockDataGenerator.generarEstacionSismologica(1);
    EstacionSismologica estacionSismologica2 = mockDataGenerator.generarEstacionSismologica(2);


    List<Estado> estados = mockDataGenerator.generarEstados();
    List<OrdenInspeccion> ordenesInspeccion = new ArrayList<>(mockDataGenerator.generarOrdenesInspeccion(empleadoRI, estacionSismologica1, estados));
    ordenesInspeccion.addAll(mockDataGenerator.generarOrdenesInspeccion(empleadoRI, estacionSismologica2, estados));
    List<TipoMotivo> listaMotivos = mockDataGenerator.generarTipoMotivo();

    // inicializamos el GestorOrden y la pantallaOrden
    GestorOrden gestorOrden = new GestorOrden();
    PantallaOrden pantallaOrden = new PantallaOrden(gestorOrden);

    // proporcionamos al gestor sus dependencias
    gestorOrden.RecibirPantallaOrden(pantallaOrden);
    gestorOrden.RecibirSesion(sesion);
    gestorOrden.RecibirOrdenesInspeccion(ordenesInspeccion);
    gestorOrden.RecibirEmpleados(empleados);
    gestorOrden.RecibirTipoMotivos(listaMotivos);
    gestorOrden.RecibirEstados(estados);

    pantallaOrden.mainProcess();
    }
}
