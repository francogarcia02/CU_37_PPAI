package com.DSI.PPAI.Red.sismica.Control;

import com.DSI.PPAI.Red.sismica.DTO.CierreOrdenRequest;
import com.DSI.PPAI.Red.sismica.Entity.*;
import com.DSI.PPAI.Red.sismica.service.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GestorOrden {

    private final OrdenInspeccionService ordenService;
    private final EmpleadoService empleadoService;
    private final SismografoService sismografoService;
    private final MotivoFueraServicioService motivoService;
    private final NotificacionService notificacionService;

    public GestorOrden(
            OrdenInspeccionService ordenService,
            EmpleadoService empleadoService,
            SismografoService sismografoService,
            MotivoFueraServicioService motivoService,
            NotificacionService notificacionService) {

        this.ordenService = ordenService;
        this.empleadoService = empleadoService;
        this.sismografoService = sismografoService;
        this.motivoService = motivoService;
        this.notificacionService = notificacionService;
    }

    @Transactional
    public void cerrarOrden(CierreOrdenRequest request, String username) {

        Empleado ri = empleadoService.obtenerPorUsername(username)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

        OrdenInspeccion orden = ordenService.obtenerPorId(request.getNumeroOrden())
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));

        if (!orden.getResponsable().equals(ri)) {
            throw new RuntimeException("No autorizado para cerrar esta orden");
        }

        if (request.getObservacionCierre() == null || request.getObservacionCierre().isBlank()) {
            throw new RuntimeException("Observación de cierre obligatoria");
        }

        if (request.getMotivos() == null || request.getMotivos().isEmpty()) {
            throw new RuntimeException("Debe seleccionar al menos un motivo");
        }

        // // Actualizar orden
        // orden.setObservacionCierre(request.getObservacionCierre());
        // orden.setEstado(EstadosOIValidos.CERRADA);
        // orden.setFechaCierre(LocalDateTime.now());
        // ordenService.guardar(orden);

        // // Actualizar sismógrafo
        // Sismografo sismografo = orden.getEstacion().getSismografo();
        // sismografo.setEstado(EstadosSismografoValidos.FUERA_SERVICIO);
        // sismografo.getCambiosEstados().add(
        //         new CambioEstado(
        //                 LocalDateTime.now(),
        //                 EstadosSismografoValidos.FUERA_SERVICIO,
        //                 request.getObservacionCierre(),
        //                 ri
        //         )
        // );
        // sismografo.setResponsableUltimoCambio(ri);
        // sismografoService.guardar(sismografo);

        // // Guardar motivos
        // List<MotivoFueraServicio> motivos = request.getMotivos().stream().map(dto -> {
        //     MotivoFueraServicio m = new MotivoFueraServicio();
        //     m.setComentario(dto.getComentario());
        //     m.setTipoMotivo(dto.getTipoMotivo());
        //     m.setSismografo(sismografo);
        //     m.setRegistradoPor(ri);
        //     m.setFechaRegistro(LocalDateTime.now());
        //     return m;
        // }).toList();

        // motivoService.guardarTodos(motivos);

        // Notificación
        //notificacionService.notificarCambioEstado(sismografo, motivos);
    }
}


