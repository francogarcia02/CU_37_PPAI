package com.DSI.PPAI.Red.sismica.service;

import com.DSI.PPAI.Red.sismica.Entity.Empleado;
import com.DSI.PPAI.Red.sismica.Repository.EmpleadoRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmpleadoService {

    private final EmpleadoRepository empleadoRepository;

    public EmpleadoService(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    public Optional<Empleado> obtenerPorUsername(String username) {
        return empleadoRepository.findByUsuarioUsername(username);
    }
}
