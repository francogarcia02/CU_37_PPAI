package control.persistencia;
import entity.OrdenInspeccion;
import entity.Estado;
import entity.MotivoFueraServicio;
import java.util.List;

public interface OrdenDAO {

    boolean guardarCierre(OrdenInspeccion ordenInspeccion, Estado estado,
                          List<MotivoFueraServicio> motivos);

}
