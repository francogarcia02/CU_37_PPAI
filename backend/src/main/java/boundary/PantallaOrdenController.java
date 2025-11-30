package boundary;

import control.GestorOrden;
import entity.OrdenInspeccion;
import entity.TipoMotivo;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class PantallaOrdenController {

    private GestorOrden gestorOrden;
    private OrdenInspeccion ordenSeleccionada;

    // --- Componentes FXML ---
    @FXML private VBox panelSeleccionOrden;
    @FXML private VBox panelFormularioCierre;
    @FXML private VBox panelDashboard;

    @FXML private ListView<OrdenInspeccion> listViewOrdenes;
    @FXML private ListView<String> listViewDashboard;
    @FXML private Label lblTituloOrden;
    @FXML private Button btnProcesar;

    // Paginación
    @FXML private Button btnPagAnterior;
    @FXML private Button btnPagSiguiente;
    @FXML private Label lblPagina;

    // Formulario
    @FXML private TextArea txtAreaObservacion;
    @FXML private FlowPane containerChips;
    @FXML private ToggleButton btnEstadoOnline;
    @FXML private ToggleButton btnEstadoOffline;
    @FXML private VBox boxMotivos;
    @FXML private ComboBox<TipoMotivo> cmbMotivos;
    @FXML private TextField txtComentarioMotivo;
    @FXML private ListView<String> listViewMotivosAgregados;

    // Notificación
    @FXML private HBox notificacionOverlay;
    @FXML private Label lblTituloNotificacion;
    @FXML private Label lblMensajeNotificacion;
    @FXML private ImageView imgIconoNotificacion;

    private ToggleGroup grupoEstado;
    private SequentialTransition currentToastAnimation;

    // Paginación Vars
    private List<OrdenInspeccion> masterListOrdenes;
    private int paginaActual = 0;
    private static final int ITEMS_POR_PAGINA = 3;

    public void setGestorOrden(GestorOrden gestorOrden) {
        this.gestorOrden = gestorOrden;
        cargarOrdenesInspeccion();
        Platform.runLater(() ->
                mostrarToast("Modo Sin Conexión", "Operando localmente. Se sincronizará al detectar red.", "WARNING")
        );
    }

    @FXML
    public void initialize() {
        listViewOrdenes.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) handleSeleccionarOrden();
        });

        grupoEstado = new ToggleGroup();
        btnEstadoOnline.setToggleGroup(grupoEstado);
        btnEstadoOffline.setToggleGroup(grupoEstado);

        grupoEstado.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null) oldVal.setSelected(true);
        });
    }

    // --- LÓGICA DE NEGOCIO ---

    private void cargarOrdenesInspeccion() {
        gestorOrden.buscarEmpleado();
        this.masterListOrdenes = gestorOrden.buscarOrdenesInspeccion();
        listViewOrdenes.setCellFactory(param -> new OrdenListCell());
        this.paginaActual = 0;
        actualizarVistaPaginacion();
    }

    private void actualizarVistaPaginacion() {
        Set<Long> procesados = new HashSet<>();
        listViewOrdenes.getItems().clear();

        if (masterListOrdenes.isEmpty()) {
            listViewOrdenes.setPlaceholder(new Label("¡Todo listo! No hay órdenes pendientes."));
            btnProcesar.setVisible(false);
            btnPagAnterior.setVisible(false);
            btnPagSiguiente.setVisible(false);
            lblPagina.setVisible(false);
            return;
        }

        btnProcesar.setVisible(true);
        btnPagAnterior.setVisible(true);
        btnPagSiguiente.setVisible(true);
        lblPagina.setVisible(true);

        int totalItems = masterListOrdenes.size();
        int totalPaginas = (int) Math.ceil((double) totalItems / ITEMS_POR_PAGINA);

        int fromIndex = paginaActual * ITEMS_POR_PAGINA;
        int toIndex = Math.min(fromIndex + ITEMS_POR_PAGINA, totalItems);

        List<OrdenInspeccion> ordenesPagina = masterListOrdenes.subList(fromIndex, toIndex);

        for (OrdenInspeccion orden : ordenesPagina) {
            if (!procesados.contains(orden.getNumeroOrden())) {
                listViewOrdenes.getItems().add(orden);
                procesados.add(orden.getNumeroOrden());
            }
        }

        lblPagina.setText(String.format("Página %d de %d", (paginaActual + 1), totalPaginas));
        btnPagAnterior.setDisable(paginaActual == 0);
        btnPagSiguiente.setDisable((paginaActual + 1) >= totalPaginas);
    }

    @FXML
    private void paginaAnterior() {
        if (paginaActual > 0) {
            paginaActual--;
            actualizarVistaPaginacion();
        }
    }

    @FXML
    private void paginaSiguiente() {
        int totalItems = masterListOrdenes.size();
        int totalPaginas = (int) Math.ceil((double) totalItems / ITEMS_POR_PAGINA);
        if (paginaActual < totalPaginas - 1) {
            paginaActual++;
            actualizarVistaPaginacion();
        }
    }

    @FXML
    private void handleSeleccionarOrden() {
        OrdenInspeccion orden = listViewOrdenes.getSelectionModel().getSelectedItem();
        if (orden == null) {
            mostrarToast("Atención", "Seleccione una tarjeta de la lista.", "WARNING");
            return;
        }
        this.ordenSeleccionada = orden;
        gestorOrden.tomarNumeroOI(ordenSeleccionada.getNumeroOrden());

        lblTituloOrden.setText("Informe de Cierre: Orden #" + ordenSeleccionada.getNumeroOrden());
        panelSeleccionOrden.setVisible(false);
        panelFormularioCierre.setVisible(true);
    }

    @FXML
    private void handleCambioEstadoSismografo() {
        boolean isOffline = btnEstadoOffline.isSelected();
        boxMotivos.setVisible(isOffline);
        boxMotivos.setManaged(isOffline);

        if (isOffline && cmbMotivos.getItems().isEmpty()) {
            cmbMotivos.getItems().setAll(gestorOrden.getTiposMotivos());
            cmbMotivos.setCellFactory(param -> new ListCell<>() {
                @Override protected void updateItem(TipoMotivo item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? "" : item.getDescripcion());
                }
            });
            cmbMotivos.setButtonCell(cmbMotivos.getCellFactory().call(null));
        }
    }

    // ¡AQUÍ ESTABA EL FALTANTE!
    @FXML
    private void handleAgregarMotivo() {
        TipoMotivo motivo = cmbMotivos.getSelectionModel().getSelectedItem();
        String comentario = txtComentarioMotivo.getText();

        txtComentarioMotivo.getStyleClass().remove("error-border");
        cmbMotivos.getStyleClass().remove("error-border");

        if (motivo != null && comentario != null && !comentario.trim().isEmpty()) {
            gestorOrden.tomarMFSyComentario(motivo, comentario);
            listViewMotivosAgregados.getItems().add("• " + motivo.getDescripcion() + " (" + comentario + ")");

            cmbMotivos.getSelectionModel().clearSelection();
            txtComentarioMotivo.clear();
        } else {
            if (motivo == null) cmbMotivos.getStyleClass().add("error-border");
            if (comentario == null || comentario.trim().isEmpty()) txtComentarioMotivo.getStyleClass().add("error-border");
            mostrarToast("Datos Incompletos", "Debe seleccionar un motivo y escribir un detalle.", "ERROR");
        }
    }

    // --- CHIPS DE TEXTO RÁPIDO ---

    @FXML
    private void insertarTextoRapido(javafx.event.ActionEvent event) {
        Button btn = (Button) event.getSource();
        String texto = btn.getText().replace("+ ", "");

        if (!txtAreaObservacion.getText().isEmpty()) txtAreaObservacion.appendText("\n");
        txtAreaObservacion.appendText("- " + texto);
        txtAreaObservacion.getStyleClass().remove("error-border");
    }

    @FXML
    private void handleCrearNuevoChip() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nuevo Comentario Rápido");
        dialog.setHeaderText(null);
        dialog.setContentText("Texto del botón:");

        DialogPane dialogPane = dialog.getDialogPane();
        try {
            dialogPane.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            dialogPane.setMinHeight(Region.USE_PREF_SIZE);
        } catch (Exception e) {}

        dialog.showAndWait().ifPresent(texto -> {
            if (!texto.trim().isEmpty()) {
                crearBotonChip(texto);
            }
        });
    }

    private void crearBotonChip(String texto) {
        Button btn = new Button(texto);
        btn.getStyleClass().add("quick-chip");
        btn.setOnAction(this::insertarTextoRapido);

        int indiceBotonNuevo = containerChips.getChildren().size() - 1;
        if (indiceBotonNuevo >= 0) {
            containerChips.getChildren().add(indiceBotonNuevo, btn);
        } else {
            containerChips.getChildren().add(btn);
        }
        mostrarToast("Agregado", "Botón rápido creado.", "INFO");
    }

    // ----------------------------------

    @FXML
    private void handleConfirmacionFinal() {
        txtAreaObservacion.getStyleClass().remove("error-border");
        txtComentarioMotivo.getStyleClass().remove("error-border");
        cmbMotivos.getStyleClass().remove("error-border");

        if (txtAreaObservacion.getText().trim().isEmpty()) {
            txtAreaObservacion.getStyleClass().add("error-border");
            mostrarToast("Falta Observación", "El campo de observaciones es obligatorio.", "ERROR");
            return;
        }

        if (btnEstadoOffline.isSelected()) {
            if (listViewMotivosAgregados.getItems().isEmpty()) {
                mostrarToast("Falta Motivo", "Debe agregar al menos una causa de falla.", "ERROR");
                return;
            }

            boolean hayTexto = !txtComentarioMotivo.getText().trim().isEmpty();
            boolean hayMotivo = cmbMotivos.getSelectionModel().getSelectedItem() != null;
            if (hayTexto || hayMotivo) {
                if(hayTexto) txtComentarioMotivo.getStyleClass().add("error-border");
                if(hayMotivo) cmbMotivos.getStyleClass().add("error-border");
                mostrarToast("Atención", "Tiene un motivo sin agregar. Añádalo o bórrelo.", "WARNING");
                return;
            }
        }

        Optional<ButtonType> result = mostrarAlertaConfirmacion("Confirmar Cierre",
                "¿Está seguro de finalizar la orden?\nSe enviarán las notificaciones correspondientes.");

        if (result.isPresent() && result.get() == ButtonType.OK) {
            gestorOrden.tomarDatosObservacion(txtAreaObservacion.getText());
            gestorOrden.tomarConfirmacioncierreOI(true);

            boolean exito = gestorOrden.cerrarOrdenSeleccionada();

            if (exito) {
                mostrarToast("¡Orden Cerrada!", "Sincronización completada con éxito.", "SUCCESS");
                resetearPantalla();
            } else {
                mostrarToast("Error de Sistema", "No se pudo cerrar la orden.", "ERROR");
            }
        }
    }

    @FXML
    private void handleVolverASeleccion() {
        resetearPantalla();
    }

    private void resetearPantalla() {
        txtAreaObservacion.clear();
        btnEstadoOnline.setSelected(true);
        // Manejo manual de visibilidad porque handleCambioEstadoSismografo depende del evento
        boxMotivos.setVisible(false);
        boxMotivos.setManaged(false);

        listViewMotivosAgregados.getItems().clear();
        gestorOrden.getMotivosFueraServicioSelection().clear();

        panelDashboard.setVisible(false);
        panelFormularioCierre.setVisible(false);
        panelSeleccionOrden.setVisible(true);
        cargarOrdenesInspeccion();
    }

    @FXML
    private void mostrarDashboard() {
        panelSeleccionOrden.setVisible(false);
        panelFormularioCierre.setVisible(false);
        panelDashboard.setVisible(true);

        listViewDashboard.getItems().clear();
        List<String> logs = SimuladorCCRS.obtenerLog();

        if (logs.isEmpty()) {
            listViewDashboard.getItems().add("> Sistema iniciado. Esperando eventos...");
        } else {
            listViewDashboard.getItems().addAll(logs);
        }

        listViewDashboard.setCellFactory(param -> new ListCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                getStyleClass().remove("log-cell");
                if (empty || item == null) {
                    setText(null); setGraphic(null);
                } else {
                    setText(item); getStyleClass().add("log-cell");
                }
            }
        });
    }

    @FXML
    public void mostrarMensajeConstruccion() {
        mostrarToast("En Desarrollo", "Este módulo estará disponible próximamente.", "INFO");
    }

    private void mostrarToast(String titulo, String mensaje, String tipo) {
        if (currentToastAnimation != null) currentToastAnimation.stop();

        lblTituloNotificacion.setText(titulo);
        lblMensajeNotificacion.setText(mensaje);

        notificacionOverlay.getStyleClass().removeAll("toast-warning", "toast-success", "toast-info", "toast-error");
        String nombreImagen = "check.png";

        if (tipo.equals("WARNING")) {
            notificacionOverlay.getStyleClass().add("toast-warning");
            nombreImagen = "offline.png";
        } else if (tipo.equals("INFO")) {
            notificacionOverlay.getStyleClass().add("toast-info");
            nombreImagen = "info.png";
        } else if (tipo.equals("ERROR")) {
            notificacionOverlay.getStyleClass().add("toast-error");
            nombreImagen = "error.png";
        } else {
            notificacionOverlay.getStyleClass().add("toast-success");
            nombreImagen = "check.png";
        }

        try {
            String ruta = "/images/" + nombreImagen;
            Image image = new Image(getClass().getResourceAsStream(ruta));
            imgIconoNotificacion.setImage(image);
        } catch (Exception e) { }

        notificacionOverlay.setVisible(true);
        notificacionOverlay.setOpacity(0);
        notificacionOverlay.setTranslateY(-50);
        notificacionOverlay.setEffect(null);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), notificacionOverlay);
        fadeIn.setToValue(1);
        TranslateTransition slideDown = new TranslateTransition(Duration.millis(300), notificacionOverlay);
        slideDown.setToY(0);
        slideDown.setInterpolator(Interpolator.EASE_OUT);

        ParallelTransition entry = new ParallelTransition(fadeIn, slideDown);
        PauseTransition stay = new PauseTransition(Duration.seconds(4));

        FadeTransition fadeOut = new FadeTransition(Duration.millis(500), notificacionOverlay);
        fadeOut.setToValue(0);
        TranslateTransition slideUp = new TranslateTransition(Duration.millis(500), notificacionOverlay);
        slideUp.setToY(-50);

        GaussianBlur blur = new GaussianBlur(0);
        notificacionOverlay.setEffect(blur);
        Timeline blurAnim = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(blur.radiusProperty(), 0)),
                new KeyFrame(Duration.millis(500), new KeyValue(blur.radiusProperty(), 10))
        );

        ParallelTransition exit = new ParallelTransition(fadeOut, slideUp, blurAnim);

        currentToastAnimation = new SequentialTransition(entry, stay, exit);
        currentToastAnimation.setOnFinished(e -> {
            notificacionOverlay.setVisible(false);
            notificacionOverlay.setEffect(null);
            currentToastAnimation = null;
        });
        currentToastAnimation.play();
    }

    private Optional<ButtonType> mostrarAlertaConfirmacion(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        DialogPane dialogPane = alert.getDialogPane();
        try {
            dialogPane.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            dialogPane.setMinHeight(Region.USE_PREF_SIZE);
        } catch (Exception e) { }
        return alert.showAndWait();
    }
}