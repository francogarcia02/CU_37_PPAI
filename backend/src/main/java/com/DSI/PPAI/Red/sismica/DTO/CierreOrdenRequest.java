package com.DSI.PPAI.Red.sismica.DTO;

import lombok.Data;

import java.util.List;

@Data
public class CierreOrdenRequest {
    private Long numeroOrden;
    private String observacionCierre;
    private List<MotivoDTO> motivos;
}

