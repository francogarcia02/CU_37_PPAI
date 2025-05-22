package com.DSI.PPAI.Red.sismica.Boundary;

import com.DSI.PPAI.Red.sismica.Control.GestorOrden;
import com.DSI.PPAI.Red.sismica.DTO.CierreOrdenRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/ordenes")
public class PantallaOrden {

    private final GestorOrden gestorOrden;

    public PantallaOrden(GestorOrden gestorOrden) {
        this.gestorOrden = gestorOrden;
    }

    @PostMapping("/cerrar")
    public ResponseEntity<Void> cerrarOrden(
            @RequestBody CierreOrdenRequest request,
            Principal principal // Spring Security: contiene el username logueado
    ) {
        gestorOrden.cerrarOrden(request, principal.getName());
        return ResponseEntity.ok().build();
    }
}
