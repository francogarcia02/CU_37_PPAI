package boundary;

import control.notificacion.DatosNotificacionCierre;
import control.notificacion.IObservadorCierreOrden;
import java.time.format.DateTimeFormatter;

public class InterfazCCRS implements IObservadorCierreOrden {

    @Override
    public void actualizar(Object objetoDatos, String evento) {
        // 1. Validar Evento
        if (!"CIERRE_ORDEN".equals(evento)) {
            return;
        }

        // 2. Validar Tipo de Datos
        if (objetoDatos instanceof DatosNotificacionCierre) {
            DatosNotificacionCierre datos = (DatosNotificacionCierre) objetoDatos;

            // 3. Delegar la comunicación al metodo especialista
            // El metodo actualizar NO sabe cómo se conecta con el CCRS, solo sabe que debe hacerlo.
            this.imprimirMonitores(datos);
        }
    }

    // Este metodo es el "Driver" o "Adaptador" que sabe hablar con el sistema externo.
    // En producción, aquí iría un "httpClient.post(...)".
    // En nuestra demo, aquí va la conexión al Simulador.
    private void imprimirMonitores(DatosNotificacionCierre datos) {

        // A. Preparar el mensaje en el formato que el sistema externo espera
        String hora = datos.getFechaHora().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String mensajeLog = String.format(
                "[%s] ALERTA SÍSMICA: Orden #%d finalizada. Estación: %s. Estado Sismógrafo: %s",
                hora,
                datos.getNumeroOrden(),
                datos.getNombreEstacion(),
                datos.getNuevoEstado()
        );

        // B. "Enviar" la señal (Simulación de conexión)
        System.out.println("INFO [InterfazCCRS] -> Enviando paquete a servidor central: " + mensajeLog);

        // Aquí se concreta la "conexión"
        SimuladorCCRS.agregarNotificacion(mensajeLog);
    }
}