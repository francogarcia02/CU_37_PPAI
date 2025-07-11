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
    // en otros CUs como "iniciar sesion" o "cargar órdenes de inspeccion"
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
    // fin de la generación de datos


    // inicializamos el GestorOrden y la pantallaOrden
    GestorOrden gestorOrden = new GestorOrden(
            ordenesInspeccion,
            empleados,
            listaMotivos,
            estados,
            sesion
    );
    PantallaOrden pantallaOrden = new PantallaOrden(
            gestorOrden
    );

    // proporcionamos al gestor la dependencia con la pantalla
    gestorOrden.RecibirPantallaOrden(pantallaOrden);

    // a partir de acá empieza el programa para el usuario

    while (!gestorOrden.getSelectedOption().equals("2")) {
        pantallaOrden.mostrarOpciones();
        if (gestorOrden.getSelectedOption().equals("1")) {
            // busca el empleado asociado a la sesion
            gestorOrden.buscarEmpleado();
            pantallaOrden.comunicarFeedbackGestorLeve("Listado de Ordenes Inspeccion para cerrar: ");
            gestorOrden.buscarOrdenesInspeccion();
            gestorOrden.pasarToPantallaOIs();
            gestorOrden.tomarNumeroOI(
                    pantallaOrden.tomarNumeroOI()
            );

            if (gestorOrden.getSelectedOrden() != null) {
                gestorOrden.tomarDatosObservacion(
                        pantallaOrden.solicitarObservacion()
                );


            gestorOrden.tomarSeleccionDecicionSismografo(
                    pantallaOrden.confirmarActualizacionSituacionSismografo()
            );

            // selectedDecicion refiere a la decisión del usuario respecto a actualizar la situación del sismografo
            if (gestorOrden.getSelectedDecicionSismografo().equals("1")) {
                gestorOrden.manageSismografoFS();
            }

            gestorOrden.tomarConfirmacioncierreOI(
                    pantallaOrden.solicitarConfirmacionCierreOI()
            );

            if (gestorOrden.getConfirmacionCierre() && gestorOrden.getObservaciones() != null){
                gestorOrden.buscarEstadoFS();
                gestorOrden.buscarEstadoCerradoOI();

                Boolean result = gestorOrden.getSelectedOrden().cerrar(
                        gestorOrden.getObservaciones(),
                        gestorOrden.getMotivosFueraServicioSelection(),
                        gestorOrden.getEstados().get(13),
                        gestorOrden.getRI());

                if (!gestorOrden.getMotivosFueraServicioSelection().isEmpty()){
                    gestorOrden.getSelectedOrden()
                            .enviarSismografoAReparar(
                                    gestorOrden.getEstadoFS()
                            );
                }

                pantallaOrden.mostrarResultadoCierre(result);

                gestorOrden.enviarNotificacionMail(
                        gestorOrden.confeccionarMensaje(
                                gestorOrden.getSelectedOrden()
                        )
                );

                gestorOrden.publicarMonitores();
            }
            }
            pantallaOrden.imprimirOndasSismicas("usted está siendo redirigido al menú principal");
        }

    }
}
}
