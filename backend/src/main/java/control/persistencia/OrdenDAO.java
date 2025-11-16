package control.persistencia;
import entity.Empleado;
import entity.OrdenInspeccion;
import entity.Estado;
import entity.MotivoFueraServicio;
import java.util.List;

public interface OrdenDAO {

    boolean guardarCierre(OrdenInspeccion ordenInspeccion, Estado estado,
                          List<MotivoFueraServicio> motivos);

    /**
     * Busca en la BD todas las órdenes en estado "Finalizado"
     * que pertenezcan al Responsable de Inspección (RI) dado.
     */
    List<OrdenInspeccion> getAllOrdenes();

    /**
     * Actualiza una orden de inspección existente en la base de datos.
     * @param orden La orden de inspección con los datos actualizados.
     */
    void update(OrdenInspeccion orden);

}
