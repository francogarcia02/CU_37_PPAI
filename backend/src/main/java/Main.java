import boundary.PantallaOrdenController;
import control.GestorOrden;
import control.persistencia.*;
import entity.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
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
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // 1. Mostrar la animación INMEDIATAMENTE (sin esperar a la BD)
        mostrarAnimacionBienvenida();

        // 2. Iniciar la carga de la Base de Datos en un hilo secundario
        iniciarCargaEnSegundoPlano();
    }

    private void mostrarAnimacionBienvenida() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Splash.fxml"));
            Scene scene = new Scene(root, 700, 450);
            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void iniciarCargaEnSegundoPlano() {
        // Creamos una Tarea (Task) que se ejecutará en otro hilo
        Task<Void> cargaTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                long tiempoInicio = System.currentTimeMillis();

                // A. Carga pesada de Hibernate y Datos
                System.out.println("Iniciando conexión con BD...");
                inicializarLogicaDeNegocio();
                System.out.println("Conexión exitosa.");

                // B. Asegurar que la animación se vea al menos 4 segundos
                // (Si la BD carga en 1 seg, esperamos 3 más para que se luzca el logo)
                long tiempoTranscurrido = System.currentTimeMillis() - tiempoInicio;
                long tiempoMinimoAnimacion = 4000; // 4 segundos

                if (tiempoTranscurrido < tiempoMinimoAnimacion) {
                    Thread.sleep(tiempoMinimoAnimacion - tiempoTranscurrido);
                }

                return null;
            }
        };

        // Cuando la tarea termine con éxito:
        cargaTask.setOnSucceeded(e -> {
            try {
                mostrarPantallaPrincipal();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        // Cuando la tarea falle (Error de conexión):
        cargaTask.setOnFailed(e -> {
            Throwable error = cargaTask.getException();
            error.printStackTrace();
            // Aquí podrías mostrar una alerta de error fatal
        });

        // ¡Arrancar el hilo!
        new Thread(cargaTask).start();
    }

    private void mostrarPantallaPrincipal() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/PantallaOrden.fxml"));
        Parent root = loader.load();

        PantallaOrdenController controller = loader.getController();
        controller.setGestorOrden(gestorOrden);

        // Cerrar el Splash (que no tiene bordes)
        primaryStage.close();

        // Crear la ventana principal nueva (con bordes normales)
        Stage mainStage = new Stage(StageStyle.DECORATED);
        mainStage.setTitle("Sistema de Red Sísmica - CCRS");

        Scene scene = new Scene(root, 1080, 720);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        mainStage.setScene(scene);
        mainStage.centerOnScreen();
        mainStage.show();
    }

    private void inicializarLogicaDeNegocio() {
        // DAOs
        EmpleadoDAO empleadoDAO = new EmpleadoDAOImpl();
        EstadoDAO estadoDAO = new EstadoDAOImpl();
        TipoMotivoDAO tipoMotivoDAO = new TipoMotivoDAOImpl();
        OrdenDAO ordenDAO = new OrdenDAOImpl();

        // Datos de Sesión (Simulados)
        Empleado empleadoLogueado = empleadoDAO.getById(1L);
        Usuario usuarioLogueado = new Usuario("AgusBieberQW", "123456", empleadoLogueado);
        Sesion sesion = new Sesion(usuarioLogueado);

        // Listas de referencia
        List<Empleado> todosLosEmpleados = empleadoDAO.getAll();
        List<Estado> todosLosEstados = estadoDAO.getAll();
        List<TipoMotivo> todosLosTiposMotivo = tipoMotivoDAO.getAll();

        // Gestor
        this.gestorOrden = new GestorOrden(
                todosLosEmpleados,
                todosLosTiposMotivo,
                todosLosEstados,
                sesion,
                ordenDAO
        );

        // Nota: Los observadores se crean bajo demanda en el gestor, no aquí.
    }

    public static void main(String[] args) {
        launch(args);
        JPAUtil.shutdown();
    }
}