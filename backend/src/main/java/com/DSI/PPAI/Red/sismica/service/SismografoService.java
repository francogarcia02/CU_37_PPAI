package com.DSI.PPAI.Red.sismica.service;

import com.DSI.PPAI.Red.sismica.Entity.Sismografo;
import com.DSI.PPAI.Red.sismica.Repository.SismografoRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SismografoService {

    private final SismografoRepository sismografoRepository;

    public SismografoService(SismografoRepository sismografoRepository) {
        this.sismografoRepository = sismografoRepository;
    }

    public Sismografo guardar(Sismografo sismografo) {
        return sismografoRepository.save(sismografo);
    }

    public Optional<Sismografo> obtenerPorId(Long id) {
        return sismografoRepository.findById(id);
    }
}

