package com.DSI.PPAI.Red.sismica.service;

import com.DSI.PPAI.Red.sismica.Entity.MotivoFueraServicio;
import com.DSI.PPAI.Red.sismica.Repository.MotivoFueraServicioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MotivoFueraServicioService {

    private final MotivoFueraServicioRepository motivoRepository;

    public MotivoFueraServicioService(MotivoFueraServicioRepository motivoRepository) {
        this.motivoRepository = motivoRepository;
    }

    public void guardarTodos(List<MotivoFueraServicio> motivos) {
        motivoRepository.saveAll(motivos);
    }

    public List<String> obtenerTiposMotivo() {
        // Suponiendo que tenés una enum o entidad para los tipos
        return List.of("Fallo de hardware", "Falta de energía", "Mantenimiento", "Otro");
    }
}
