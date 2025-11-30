package boundary;

import entity.OrdenInspeccion;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class OrdenListCell extends ListCell<OrdenInspeccion> {

    @FXML private HBox cardContainer;
    @FXML private Label lblNombreEstacion;
    @FXML private Label lblNumeroOrden;
    @FXML private Label lblModelo;
    @FXML private Label lblProvincia;
    @FXML private ImageView imgIcono;
    @FXML private Circle iconBackground;
    @FXML private Button btnMapa;

    private FXMLLoader mLLoader;

    @Override
    protected void updateItem(OrdenInspeccion orden, boolean empty) {
        super.updateItem(orden, empty);

        if (empty || orden == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (mLLoader == null) {
                mLLoader = new FXMLLoader(getClass().getResource("/ItemOrden.fxml"));
                mLLoader.setController(this);
                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // --- 1. DATOS BÁSICOS ---
            lblNombreEstacion.setText(orden.getEstacionSismologica().getNombreEstacion());
            lblNumeroOrden.setText("Orden de Inspección #" + orden.getNumeroOrden());
            lblModelo.setText(orden.getEstacionSismologica().getSismografo().getModelo());

            // --- 2. LÓGICA VISUAL DE "RELLENO" (Simulación de Contexto) ---
            double lat = orden.getEstacionSismologica().getLatitud();
            configurarEstiloPorUbicacion(orden.getEstacionSismologica().getLatitud(), orden.getEstacionSismologica().getNombreEstacion());
            // --- 3. BOTÓN DE MAPA (Google Maps) ---
            btnMapa.setOnAction(event -> abrirGoogleMaps(
                    orden.getEstacionSismologica().getLatitud(),
                    orden.getEstacionSismologica().getLongitud()
            ));

            // Evitar que el click en el botón seleccione la celda de la lista
            btnMapa.setOnMouseClicked(event -> event.consume());

            setText(null);
            setGraphic(cardContainer);
        }
    }

    private void configurarEstiloPorUbicacion(double lat, String nombreEstacion) {
        iconBackground.setStrokeWidth(2);
        String nombreImagen = "";

        if (lat < -60) {
            // ANTÁRTIDA
            lblProvincia.setText("ANTÁRTIDA");
            nombreImagen = "hielo.png";
            iconBackground.setStroke(Color.web("#3498db"));
            iconBackground.setFill(Color.web("#d6eaf8"));

        } else if (lat < -38) {
            // PATAGONIA
            lblProvincia.setText("PATAGONIA");
            nombreImagen = "montana_nieve.png";
            iconBackground.setStroke(Color.web("#8e44ad"));
            iconBackground.setFill(Color.web("#ebdef0"));

        } else if (nombreEstacion.contains("Pilar")) {
            // --- CASO ESPECIAL PARA PILAR (LLANO) ---
            lblProvincia.setText("CÓRDOBA (LLANO)");
            nombreImagen = "ciudad.png";
            iconBackground.setStroke(Color.web("#e67e22")); // Naranja
            iconBackground.setFill(Color.web("#fae5d3")); // Arena

        } else {
            // SIERRA DE LA INVERNADA / OTRAS (Estaciones de Montaña)
            // Este bloque 'else' es donde cae Bosque Alegre
            lblProvincia.setText("CÓRDOBA (SIERRAS)");
            nombreImagen = "montana.png";
            iconBackground.setStroke(Color.web("#27ae60")); // Verde
            iconBackground.setFill(Color.web("#eaeded")); // Gris
        }

        // Carga de imagen
        try {
            if (!nombreImagen.isEmpty()) {
                String rutaImagen = "/images/" + nombreImagen;
                Image image = new Image(getClass().getResourceAsStream(rutaImagen));
                imgIcono.setImage(image);
            }
        } catch (Exception e) {
            System.err.println("Error al cargar imagen: " + nombreImagen);
        }
    }

    private void abrirGoogleMaps(double lat, double lon) {
        try {
            // URL oficial de Google Maps con coordenadas
            String url = "https://www.google.com/maps/search/?api=1&query=" + lat + "," + lon;

            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(url));
            } else {
                System.out.println("No se puede abrir el navegador automáticamente. URL: " + url);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}