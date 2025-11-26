package control.notificacion;

import entity.MotivoFueraServicio;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter // Genera automáticamente TODOS los getters
@AllArgsConstructor // Genera automáticamente un constructor con TODOS los campos
public class DatosNotificacionCierre {

    private final List<String> emailsDestinatarios; // AGREGADO
    private final String sismografoId;
    private final String nuevoEstado;
    private final LocalDateTime fechaHora;
//    private final List<MotivoFueraServicio> motivos;
    private final List<String> motivos; // CORRECCIÓN: Ahora es una lista de Strings, no de Entidades
    private final Long numeroOrden;
    private final String nombreEstacion;
    private final String nombreResponsable;

    // Lombok generará los getters y el constructor
    // en tiempo de compilación.
}