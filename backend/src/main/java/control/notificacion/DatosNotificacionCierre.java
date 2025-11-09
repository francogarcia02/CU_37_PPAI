package control.notificacion;

import entity.MotivoFueraServicio;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter // Genera automáticamente TODOS los getters
@AllArgsConstructor // Genera automáticamente un constructor con TODOS los campos
public class DatosNotificacionCierre {

    private final String sismografoId;
    private final String nuevoEstado;
    private final LocalDateTime fechaHora;
    private final List<MotivoFueraServicio> motivos;

    private final Long numeroOrden;
    private final String nombreEstacion;
    private final String nombreResponsable;

    // Lombok generará los 7 getters y el constructor con los 7 argumentos
    // en tiempo de compilación.
}