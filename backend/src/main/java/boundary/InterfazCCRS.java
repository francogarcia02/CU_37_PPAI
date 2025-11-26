package boundary;

import control.notificacion.DatosNotificacionCierre;
import control.notificacion.IObservadorCierreOrden;

public class InterfazCCRS implements IObservadorCierreOrden {

    @Override
    public void actualizar(Object objetoDatos, String evento) {

        // 1. Validar Evento (Semántica)
        // Si el evento no es el esperado, ignoramos la notificación.
        if (!"CIERRE_ORDEN".equals(evento)) {
            return;
        }

        // 2. Casteo Seguro (Técnica)
        // Verificamos que el objeto sea del tipo esperado antes de usarlo.
        if (objetoDatos instanceof DatosNotificacionCierre) {
            DatosNotificacionCierre datos = (DatosNotificacionCierre) objetoDatos;

            // 3. Lógica de Presentación
            System.out.println(String.format(
                    "INFO [InterfazCCRS] -> Evento recibido: %s. Procesando actualización para Orden [%d]...",
                    evento,
                    datos.getNumeroOrden()
            ));

            // Pasamos los datos necesarios al metodo interno
            this.imprimirMonitores(datos.getNuevoEstado(), datos.getSismografoId());
        }
    }

    // Metodo interno que simula la actualización de una pantalla física o dashboard
    private void imprimirMonitores(String nuevoEstado, String idSismografo) {
        System.out.println("**************************************************");
        System.out.println("* SISTEMA DE MONITOREO CCRS - ALERTA        *");
        System.out.println("**************************************************");
        System.out.printf ("* SISMOGRAFO ID: %-31s *%n", idSismografo);
        System.out.printf ("* NUEVO ESTADO : %-31s *%n", nuevoEstado);
        System.out.println("**************************************************");
        System.out.println(">> Publicacion en monitores completada exitosamente.\n");
    }
}