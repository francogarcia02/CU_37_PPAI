package boundary;

import control.GestorOrden;
import entity.OrdenInspeccion;
import entity.TipoMotivo;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.util.List;
import java.util.Optional;

public class PantallaOrdenController {

    private GestorOrden gestorOrden;
    private OrdenInspeccion ordenSeleccionada;

    // --- Componentes FXML ---
    @FXML private Label lblPasoActual;
    @FXML private ListView<String> listViewOrdenes;
    @FXML private TextArea txtAreaObservacion;
    @FXML private CheckBox chkPonerFueraDeServicio;
    @FXML private ComboBox<TipoMotivo> cmbMotivos;
    @FXML private TextField txtComentarioMotivo;
    @FXML private ListView<String> listViewMotivosAgregados;
    @FXML private Button btnConfirmarCierre;

    // --- Paneles para el flujo secuencial ---
    @FXML private VBox panelSeleccionOrden;
    @FXML private VBox panelIngresoObservacion;
    @FXML private VBox panelGestionSismografo;
    @FXML private VBox panelConfirmacionFinal;

    @FXML
    public void initialize() {
        cambiarVisibilidadPaneles(true, false, false, false);
    }

    public void setGestorOrden(GestorOrden gestorOrden) {
        this.gestorOrden = gestorOrden;
        cargarOrdenesInspeccion();
    }

    private void cargarOrdenesInspeccion() {
        gestorOrden.buscarEmpleado();
        List<OrdenInspeccion> ordenes = gestorOrden.buscarOrdenesInspeccion();

        // Limpiamos la lista antes de volver a cargarla
        listViewOrdenes.getItems().clear();

        if (ordenes.isEmpty()) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Información", "¡Felicitaciones! No hay más órdenes de inspección para cerrar.");
            panelSeleccionOrden.setDisable(true);
        } else {
            panelSeleccionOrden.setDisable(false);
            for (OrdenInspeccion orden : ordenes) {
                listViewOrdenes.getItems().add(String.format("Orden N°: %d - Estación: %s", orden.getNumeroOrden(), orden.getEstacionSismologica().getNombreEstacion()));
            }
        }
    }

    @FXML
    private void handleSeleccionarOrden() {
        int indiceSeleccionado = listViewOrdenes.getSelectionModel().getSelectedIndex();
        if (indiceSeleccionado < 0) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selección Requerida", "Por favor, seleccione una orden de la lista.");
            return;
        }
        this.ordenSeleccionada = gestorOrden.getOrdenesInspeccionFiltradas().get(indiceSeleccionado);
        gestorOrden.tomarNumeroOI(ordenSeleccionada.getNumeroOrden());

        lblPasoActual.setText("Paso 2: Ingresar Observación de Cierre");
        cambiarVisibilidadPaneles(false, true, false, false);
    }

    @FXML
    private void handleGuardarObservacion() {
        String observacion = txtAreaObservacion.getText();
        if (observacion == null || observacion.trim().isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campo Requerido", "La observación no puede estar vacía.");
            return;
        }
        gestorOrden.tomarDatosObservacion(observacion);

        lblPasoActual.setText("Paso 3: Actualizar Situación del Sismógrafo (Opcional)");
        cambiarVisibilidadPaneles(false, false, true, false);
        panelGestionSismografo.getChildren().get(1).setVisible(false); // Ocultar detalles de motivo
    }

    @FXML
    private void handleCheckFueraDeServicio() {
        boolean seleccionado = chkPonerFueraDeServicio.isSelected();
        panelGestionSismografo.getChildren().get(1).setVisible(seleccionado);
        if (seleccionado && cmbMotivos.getItems().isEmpty()) {
            cmbMotivos.getItems().setAll(gestorOrden.getTiposMotivos());
            cmbMotivos.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(TipoMotivo item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? null : item.getDescripcion());
                }
            });
        }
    }

    @FXML
    private void handleAgregarMotivo() {
        TipoMotivo motivoSeleccionado = cmbMotivos.getSelectionModel().getSelectedItem();
        String comentario = txtComentarioMotivo.getText();

        if (motivoSeleccionado == null || comentario == null || comentario.trim().isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Datos Incompletos", "Debe seleccionar un motivo e ingresar un comentario.");
            return;
        }

        gestorOrden.tomarMFSyComentario(motivoSeleccionado, comentario);
        listViewMotivosAgregados.getItems().add(motivoSeleccionado.getDescripcion() + ": " + comentario);

        cmbMotivos.getSelectionModel().clearSelection();
        txtComentarioMotivo.clear();
    }

    @FXML
    private void handleConfirmarGestionSismografo() {
        if (chkPonerFueraDeServicio.isSelected() && gestorOrden.getMotivosFueraServicioSelection().isEmpty()){
            mostrarAlerta(Alert.AlertType.WARNING, "Motivo Requerido", "Si pone el sismógrafo fuera de servicio, debe agregar al menos un motivo.");
            return;
        }

        lblPasoActual.setText("Paso 4: Confirmación Final");
        btnConfirmarCierre.setText("Confirmar Cierre Definitivo de la Orden N° " + ordenSeleccionada.getNumeroOrden());
        cambiarVisibilidadPaneles(false, false, false, true);
    }

    @FXML
    private void handleConfirmacionFinal() {
        Optional<ButtonType> resultado = mostrarAlertaConfirmacion("Confirmar Cierre", "¿Está seguro de que desea cerrar esta orden de inspección? Esta acción no se puede deshacer.");

        if(resultado.isPresent() && resultado.get() == ButtonType.OK) {
            gestorOrden.tomarConfirmacioncierreOI(true);
            boolean exito = gestorOrden.cerrarOrdenSeleccionada();

            if (exito) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "La orden de inspección se ha cerrado correctamente.");
                // ¡CAMBIO CLAVE AQUÍ! En lugar de cerrar, reiniciamos la pantalla.
                resetearPantalla();
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "Ocurrió un error al intentar cerrar la orden.");
            }
        }
    }

    /**
     * Nuevo método para reiniciar la interfaz a su estado inicial.
     */
    private void resetearPantalla() {
        // 1. Limpiar variables de estado
        ordenSeleccionada = null;
        gestorOrden.getMotivosFueraServicioSelection().clear();

        // 2. Limpiar controles de la UI
        txtAreaObservacion.clear();
        chkPonerFueraDeServicio.setSelected(false);
        cmbMotivos.getSelectionModel().clearSelection();
        txtComentarioMotivo.clear();
        listViewMotivosAgregados.getItems().clear();

        // 3. Volver a cargar la lista de órdenes actualizada
        cargarOrdenesInspeccion();

        // 4. Mostrar el primer panel y resetear el título
        lblPasoActual.setText("Paso 1: Seleccionar Orden de Inspección");
        cambiarVisibilidadPaneles(true, false, false, false);
    }

    // --- Métodos de utilidad ---
    private void cambiarVisibilidadPaneles(boolean p1, boolean p2, boolean p3, boolean p4) {
        panelSeleccionOrden.setVisible(p1); panelSeleccionOrden.setManaged(p1);
        panelIngresoObservacion.setVisible(p2); panelIngresoObservacion.setManaged(p2);
        panelGestionSismografo.setVisible(p3); panelGestionSismografo.setManaged(p3);
        panelConfirmacionFinal.setVisible(p4); panelConfirmacionFinal.setManaged(p4);
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private Optional<ButtonType> mostrarAlertaConfirmacion(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        return alert.showAndWait();
    }
}