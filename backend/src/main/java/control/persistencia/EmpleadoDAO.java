package control.persistencia;

import entity.Empleado;
import java.util.List;

public interface EmpleadoDAO {
    Empleado getById(Long id);
    List<Empleado> getAll();
}
