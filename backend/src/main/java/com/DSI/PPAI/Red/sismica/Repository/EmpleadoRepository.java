package com.DSI.PPAI.Red.sismica.Repository;

import com.DSI.PPAI.Red.sismica.Entity.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
    Optional<Empleado> findByUsuarioUsername(String username);
}
