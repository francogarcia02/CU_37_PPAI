import boundary.PantallaOrdenController;
import control.GestorOrden;
import control.persistencia.*;
import entity.*;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import boundary.InterfazCCRS;
import boundary.InterfazMail;
import control.notificacion.IObservadorCierreOrden;

public class Main extends Application {

    private Stage primaryStage;
    private GestorOrden gestorOrden;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        // 1. Inicializa la lógica de negocio con datos reales de la BD
        inicializarLogicaDeNegocio();

        // 2. Muestra la animación de bienvenida
        mostrarAnimacionBienvenida();
    }

    private void mostrarAnimacionBienvenida() {
        WebView webView = new WebView();
        URL url = getClass().getResource("/utnLogohtml/index.html");
        webView.getEngine().load(url.toExternalForm());

        StackPane root = new StackPane(webView);
        Scene scene = new Scene(root, 800, 600);

        primaryStage.setTitle("Cargando...");
        primaryStage.setScene(scene);
        primaryStage.show();

        PauseTransition delay = new PauseTransition(Duration.seconds(4));
        delay.setOnFinished(event -> {
            Platform.runLater(() -> {
                try {
                    mostrarPantallaPrincipal();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        });
        delay.play();
    }

    private void mostrarPantallaPrincipal() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/PantallaOrden.fxml"));
        Parent root = loader.load();

        PantallaOrdenController controller = loader.getController();
        controller.setGestorOrden(gestorOrden);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Sistema de Red Sísmica - CCRS");
        primaryStage.setScene(scene);
    }

    private void inicializarLogicaDeNegocio() {
        // --- Carga de datos desde la Base de Datos usando DAOs ---
        EmpleadoDAO empleadoDAO = new EmpleadoDAOImpl();
        EstadoDAO estadoDAO = new EstadoDAOImpl();
        TipoMotivoDAO tipoMotivoDAO = new TipoMotivoDAOImpl();

        // Simulamos el inicio de sesión del Responsable de Inspecciones (ID 1)
        Empleado empleadoLogueado = empleadoDAO.getById(1L);
        Usuario usuarioLogueado = new Usuario("AgusBieberQW", "123456", empleadoLogueado);
        Sesion sesion = new Sesion(usuarioLogueado);

        // Obtenemos todos los datos necesarios para el gestor
        List<Empleado> todosLosEmpleados = empleadoDAO.getAll();
        List<Estado> todosLosEstados = estadoDAO.getAll();
        System.out.println("Todos los estados: " + todosLosEstados);
        List<TipoMotivo> todosLosTiposMotivo = tipoMotivoDAO.getAll();

        // Creamos el gestor con los datos reales
        this.gestorOrden = new GestorOrden(
                todosLosEmpleados,
                todosLosTiposMotivo,
                todosLosEstados,
                sesion
        );

        // --- Configuración del Patrón Observer ---
        List<String> mailsDeReparacion = this.gestorOrden.obtenerMailsResponsablesReparacion();
        IObservadorCierreOrden observadorMail = new InterfazMail(mailsDeReparacion);
        IObservadorCierreOrden observadorCCRS = new InterfazCCRS();

        this.gestorOrden.agregarObservador(observadorMail);
        this.gestorOrden.agregarObservador(observadorCCRS);
    }

    public static void main(String[] args) {
        launch(args);
        // Es una buena práctica cerrar la fábrica de EntityManager cuando la aplicación termina.
        JPAUtil.shutdown();
    }
}
