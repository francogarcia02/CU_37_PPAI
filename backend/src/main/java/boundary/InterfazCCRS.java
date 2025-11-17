package boundary;
import control.notificacion.DatosNotificacionCierre;
import control.notificacion.IObservadorCierreOrden;

public class InterfazCCRS implements IObservadorCierreOrden {

    @Override
    public void actualizar(DatosNotificacionCierre datos) {

        System.out.println(String.format(
                "INFO [InterfazCCRS] -> Evento recibido. Publicando estado de Orden [%d] (Sismógrafo: %s) en monitores...",
                datos.getNumeroOrden(),
                datos.getNuevoEstado()
        ));

        this.imprimirMonitores(); // Llama al metodo original
    }


    private void imprimirMonitores() {
    // -- Aca iria la logica para actualizar los Monitores
        System.out.println("Publicacion de monitores CCRS completada"); // Placeholder
    }
};