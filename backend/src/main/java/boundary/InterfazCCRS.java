package boundary;
import control.notificacion.DatosNotificacionCierre;
import control.notificacion.IObservadorCierreOrden;

public class InterfazCCRS implements IObservadorCierreOrden {

    @Override
    public void actualizar(DatosNotificacionCierre datos) {

        // (OPCIONAL) Hacemos la notificación más informativa
        System.out.println("--- PUBLICANDO EN MONITORES CCRS ---");
        System.out.println("Orden " + datos.getNumeroOrden() + " cerrada.");
        System.out.println("Sismografo " + datos.getSismografoId() + " paso a estado: " + datos.getNuevoEstado());

        this.imprimirMonitores(); // Llama al metodo original

        System.out.println("------------------------------------");
    }


    private void imprimirMonitores() {
    //
        System.out.println("Publicacion de monitores CCRS completada"); // Placeholder
    }
};