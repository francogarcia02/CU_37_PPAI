package boundary;

import control.notificacion.DatosNotificacionCierre;
import control.notificacion.IObservadorCierreOrden;
import java.util.List;
import java.util.stream.Collectors;

// Implementa la interfaz del patron.
public class InterfazMail implements IObservadorCierreOrden {

    // Recibe solo una lista de Strings (emails)
    private final List<String> mailsDestinatarios;

    public InterfazMail(List<String> mailsDestinatarios) {
        this.mailsDestinatarios = mailsDestinatarios;
    }

    @Override
    public void actualizar(DatosNotificacionCierre datos) {

        // 1. Loguear si se recibe el evento pero no se actúa
        if (datos.getMotivos() == null || datos.getMotivos().isEmpty()) {
            System.out.println(String.format(
                    "INFO [InterfazMail] -> Evento recibido. Orden [%d] sin motivos, no se requiere notificación.",
                    datos.getNumeroOrden()
            ));
            return;
        }

        // 2. Loguear la decisión de actuar
        System.out.println(String.format(
                "INFO [InterfazMail] -> Evento recibido. Sismógrafo '%s'. Se requiere notificación para Orden [%d].",
                datos.getNuevoEstado(),
                datos.getNumeroOrden()
        ));

        // La lógica de envío ahora es simple
        String mensaje = confeccionarMensaje(datos);

        // Usa la lista de mails que ya tiene configurada
        // 3. --- ESTE METODO NO SE CAMBIA ---
        // Sigue imprimiendo el String que devuelve enviarMail.
        // Ahora, ese String será limpio.
        mailsDestinatarios.forEach(mail ->
                System.out.println(this.enviarMail(mail, mensaje)));
        };

    private String confeccionarMensaje(DatosNotificacionCierre datos) {
        String motivosStr = datos.getMotivos() != null ?
                datos.getMotivos().stream()
                        .map(motivo -> String.format("  - Motivo: %s\n    Observaciones: %s\n",
                                motivo.getTipoMotivo().getDescripcion(),
                                motivo.getComentario() != null ? motivo.getComentario() : "Sin observaciones"))
                        .collect(Collectors.joining("\n")) :
                "  No hay motivos registrados\n";

        return String.format(
                "Estimado(a) responsable de reparaciones,\n\n" +
                        "La Orden de Inspeccion %d ha sido cerrada.\n\n" +      // 1. numeroOrden
                        "Detalles:\n" +
                        "Estacion Sismologica: %s\n" +                           // 2. nombreEstacion
                        "Responsable de la Orden: %s\n" +                       // 3. nombreResponsable
                        "ID sismografo: %s\n\n" +                               // 4. sismografoId
                        "Estado actual del sismógrafo: %s\n" +                  // 5. nuevoEstado
                        "Fecha y hora nuevo estado: %s\n" +                     // 6. fechaHora
                        "Motivos:\n%s",                                        // 7. motivosStr

                // --- 7 ARGUMENTOS ---
                datos.getNumeroOrden(),
                datos.getNombreEstacion(),
                datos.getNombreResponsable(),
                datos.getSismografoId(),
                datos.getNuevoEstado(),
                datos.getFechaHora().toString(),
                motivosStr
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



