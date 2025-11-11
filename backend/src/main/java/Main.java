import boundary.PantallaOrdenController;
import control.GestorOrden;
import control.MOCKDATAGenerator;
import control.notificacion.IObservadorCierreOrden;
import entity.*;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import boundary.InterfazCCRS; // por patron observador.
import boundary.InterfazMail;


public class Main extends Application {

    private Stage primaryStage;
    private GestorOrden gestorOrden;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        // 1. Inicializa toda la lógica de negocio primero
        inicializarLogicaDeNegocio();

        // 2. Muestra la animación de bienvenida
        mostrarAnimacionBienvenida();
    }

    private void mostrarAnimacionBienvenida() {
        WebView webView = new WebView();
        // Le decimos que cargue el index.html de nuestra carpeta de recursos
        URL url = getClass().getResource("/utnLogohtml/index.html");
        webView.getEngine().load(url.toExternalForm());

        StackPane root = new StackPane(webView);
        Scene scene = new Scene(root, 800, 600);

        primaryStage.setTitle("Cargando...");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Crea una pausa para la animación
        PauseTransition delay = new PauseTransition(Duration.seconds(4));
        delay.setOnFinished(event -> {
            try {
                // 4. Cuando la pausa termina, carga la pantalla principal
                mostrarPantallaPrincipal();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        delay.play();
    }

    private void mostrarPantallaPrincipal() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/PantallaOrden.fxml"));
        Parent root = loader.load();

        PantallaOrdenController controller = loader.getController();
        controller.setGestorOrden(gestorOrden); // Le pasamos el gestor al controlador

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Sistema de Red Sísmica - CCRS");
        primaryStage.setScene(scene);
    }

    private void inicializarLogicaDeNegocio() {
        MOCKDATAGenerator mockDataGenerator = new MOCKDATAGenerator();
        Empleado empleadoRI = mockDataGenerator.generarEmpleado(1);
        Usuario usuarioRI = mockDataGenerator.generarUsuario(1);
        Empleado otroEmpleadoNoRI = mockDataGenerator.generarEmpleado(2);
        List<Empleado> empleados = List.of(empleadoRI, otroEmpleadoNoRI);
        Sesion sesion = mockDataGenerator.generarSesion(usuarioRI);
        EstacionSismologica estacionSismologica1 = mockDataGenerator.generarEstacionSismologica(1);
        EstacionSismologica estacionSismologica2 = mockDataGenerator.generarEstacionSismologica(2);
        List<Estado> estados = mockDataGenerator.generarEstados();
        List<OrdenInspeccion> ordenesInspeccion = new ArrayList<>(mockDataGenerator.generarOrdenesInspeccion(empleadoRI, estacionSismologica1, estados));
        ordenesInspeccion.addAll(mockDataGenerator.generarOrdenesInspeccion(empleadoRI, estacionSismologica2, estados));
        List<TipoMotivo> listaMotivos = mockDataGenerator.generarTipoMotivo();

        this.gestorOrden = new GestorOrden(
                ordenesInspeccion,
                empleados,
                listaMotivos,
                estados,
                sesion
        );

        // --- INICIO "ENSAMBLADO" OBSERVER

        // 1  CONSULTA al Gestor por la data de configuración
        List<String> mailsDeReparacion = this.gestorOrden.obtenerMailsResponsablesReparacion();

        // 2. CREA LOS OBSERVADORES
        IObservadorCierreOrden observadorMail = new InterfazMail(mailsDeReparacion); // solo Strings
        IObservadorCierreOrden observadorCCRS = new InterfazCCRS();

        // 3. SUSCRIBE LOS OBSERVADORES AL SUJETO
        this.gestorOrden.agregarObservador(observadorMail);
        this.gestorOrden.agregarObservador(observadorCCRS);
        // --- FIN "ENSAMBLADO" OBSERVER ---

    }

    public static void main(String[] args) {
        launch(args);
    }
}