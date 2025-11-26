package boundary;

import control.notificacion.DatosNotificacionCierre;
import control.notificacion.IObservadorCierreOrden;
import java.util.List;
import java.util.stream.Collectors;

// Implementa la interfaz del patron.
public class InterfazMail implements IObservadorCierreOrden {

    //    // Recibe solo una lista de Strings (emails)
    //    private final List<String> mailsDestinatarios;

    //    public InterfazMail(List<String> mailsDestinatarios) {
    //        this.mailsDestinatarios = mailsDestinatarios;
    //    }

    @Override
    public void actualizar(Object objetoDatos, String evento) {

        if (!"CIERRE_ORDEN".equals(evento)) {
            return;
        }

        // 2. Casteo seguro (Técnica)
        if (objetoDatos instanceof DatosNotificacionCierre) {
            DatosNotificacionCierre datos = (DatosNotificacionCierre) objetoDatos;

            // 3. Usar los datos del DTO
            if (datos.getMotivos() == null || datos.getMotivos().isEmpty()) {
                // Logica de no enviar
                return;
            }

            String mensaje = confeccionarMensaje(datos);

            // 4. Obtener destinatarios DEL DTO (No del Main)
            List<String> destinatarios = datos.getEmailsDestinatarios();

            destinatarios.forEach(mail ->
                    System.out.println(this.enviarMail(mail, mensaje))
            );

            // 2. Loguear la decisión de actuar
            System.out.println(String.format(
                    "INFO [InterfazMail] -> Evento recibido. Sismografo '%s'. Se requiere notificacion para Orden [%d].",
                    datos.getNuevoEstado(),
                    datos.getNumeroOrden()
            ));
        }
    }

    private String confeccionarMensaje(DatosNotificacionCierre datos) {
        // Ahora 'datos.getMotivos()' ya es una List<String>, no hay que mapear nada complejo.
        String motivosStr = (datos.getMotivos() != null && !datos.getMotivos().isEmpty())
                ? String.join("\n", datos.getMotivos()) // Une los strings con saltos de linea
                : "  No hay motivos registrados";

        return String.format(
                "Estimado(a) responsable de reparaciones,\n\n" +
                        "La Orden de Inspeccion %d ha sido cerrada.\n\n" +
                        "Detalles:\n" +
                        "Estacion Sismologica: %s\n" +
                        "Responsable de la Orden: %s\n" +
                        "ID sismografo: %s\n\n" +
                        "Estado actual del sismógrafo: %s\n" +
                        "Fecha y hora nuevo estado: %s\n" +
                        "Motivos:\n%s",
                datos.getNumeroOrden(),
                datos.getNombreEstacion(),
                datos.getNombreResponsable(),
                datos.getSismografoId(),
                datos.getNuevoEstado(),
                datos.getFechaHora().toString(),
                motivosStr // Inserta el String ya procesado
        );
    }


    public String enviarMail(String mail, String mensaje) {
        // Aquí iría la lógica real de la API de email (que no cambia)

        // En lugar de devolver el mensaje, devuelve SOLO la confirmación.
        return String.format(
                "INFO [InterfazMail] -> Email enviado exitosamente a: %s",
                mail
        );

        // Este metodo no deberia devolver el mensaje, solo una confirmacion.
        // return "mail enviado a " + mail + " con el mensaje: " + mensaje; // LÍNEA ANTERIOR (ELIMINADA)
    }
}

