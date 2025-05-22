package com.DSI.PPAI.Red.sismica.service;

import com.DSI.PPAI.Red.sismica.Entity.MotivoFueraServicio;
import com.DSI.PPAI.Red.sismica.Entity.Sismografo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificacionService {

    public void notificarCambioEstado(Sismografo sismografo, List<MotivoFueraServicio> motivos) {
        // Aquí iría la lógica de:
        // - Enviar mail a empleados con rol REPARADOR
        // - Publicar en monitores del CCRS
        // (por ahora se simula con logs)
        System.out.println("🔔 Notificación: Sismógrafo " + sismografo.getId() + " fuera de servicio.");
        motivos.forEach(m -> System.out.println("📝 Motivo: " + m.getTipoMotivo() + " - " + m.getComentario()));
    }
}

