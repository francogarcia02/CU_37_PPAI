package com.DSI.PPAI.Red.sismica.service;

import com.DSI.PPAI.Red.sismica.Entity.EstadosOIValidos;
import com.DSI.PPAI.Red.sismica.Entity.OrdenInspeccion;
import com.DSI.PPAI.Red.sismica.Repository.OrdenInspeccionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrdenInspeccionService {

    private final OrdenInspeccionRepository ordenRepository;

    public OrdenInspeccionService(OrdenInspeccionRepository ordenRepository) {
        this.ordenRepository = ordenRepository;
    }

    public Optional<OrdenInspeccion> obtenerPorId(Long id) {
        return ordenRepository.findByNumeroOrden(id);
    }

    public List<OrdenInspeccion> obtenerTodas() {
        return ordenRepository.findAllByOrderByFechaFinalizacionDesc();
    }

    public OrdenInspeccion guardar(OrdenInspeccion orden) {
        return ordenRepository.save(orden);
    }
}

