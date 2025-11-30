package boundary;

import java.util.ArrayList;
import java.util.List;

// Esta clase simula ser el Servidor del CCRS que recibe las alertas
public class SimuladorCCRS {

    private static final List<String> logNotificaciones = new ArrayList<>();

    public static void agregarNotificacion(String mensaje) {
        // Agregamos al inicio para que lo más nuevo quede arriba
        logNotificaciones.add(0, mensaje);
    }

    public static List<String> obtenerLog() {
        return logNotificaciones;
    }
}