package boundary;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebView;
import java.net.URL;
import java.util.ResourceBundle;

public class SplashController implements Initializable {

    @FXML
    private WebView webViewAnimacion;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Cargar la animación HTML local
        URL urlAnimacion = getClass().getResource("/utnLogohtml/index.html");

        if (urlAnimacion != null) {
            // Hacemos que el fondo del WebView sea transparente para que se integre
            // (Nota: asegúrate que tu HTML también tenga fondo transparente o del mismo color #2c3e50)
            webViewAnimacion.setPageFill(javafx.scene.paint.Color.TRANSPARENT);
            webViewAnimacion.getEngine().load(urlAnimacion.toExternalForm());
        } else {
            System.err.println("No se encontró la animación en /utnLogohtml/index.html");
        }
    }
}