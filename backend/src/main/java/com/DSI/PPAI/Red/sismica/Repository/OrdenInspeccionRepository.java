package com.DSI.PPAI.Red.sismica.Repository;
import com.DSI.PPAI.Red.sismica.Entity.Estado;
import com.DSI.PPAI.Red.sismica.Entity.OrdenInspeccion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface OrdenInspeccionRepository extends JpaRepository<OrdenInspeccion, Long> {

    Optional<OrdenInspeccion> findByNumeroOrden(Long id);

    List<OrdenInspeccion> findAllByOrderByFechaFinalizacionDesc();
}

