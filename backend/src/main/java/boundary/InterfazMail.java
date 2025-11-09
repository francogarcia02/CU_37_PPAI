package boundary;

import control.notificacion.DatosNotificacionCierre;
import control.notificacion.IObservadorCierreOrden;
import java.util.List; // Importar
import java.util.stream.Collectors; // Importar

// Implementa la interfaz del patron.
public class InterfazMail implements IObservadorCierreOrden {

    // Recibe solo una lista de Strings (emails)
    private final List<String> mailsDestinatarios;

    public InterfazMail(List<String> mailsDestinatarios) {
        this.mailsDestinatarios = mailsDestinatarios;
    }

    @Override
    public void actualizar(DatosNotificacionCierre datos) {

        // 5. La lógica de decisión sigue aquí (perfecto)
        if (datos.getMotivos() == null || datos.getMotivos().isEmpty()) {
            return; // No hago nada.
        }

        // 6. La lógica de envío ahora es simple
        String mensaje = confeccionarMensaje(datos);

        // Usa la lista de mails que ya tiene configurada
        mailsDestinatarios.forEach(mail ->
                System.out.println(this.enviarMail(mail, mensaje)));
    }

    // 8. El metodo `confeccionarMensaje`
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
        return "mail enviado a " + mail + " con el mensaje: " + mensaje;
    }
}



