package com.capricho.ms_inventario.controller;

import com.capricho.ms_inventario.dto.DescontarStockRequest;
import com.capricho.ms_inventario.service.InventarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventario")
@RequiredArgsConstructor
public class InventarioController {

    private final InventarioService inventarioService;

    /**
     * Endpoint que consumirá el microservicio de ventas.
     */
    @PostMapping("/descontar")
    public ResponseEntity<String> descontarStock(
            @RequestBody DescontarStockRequest request) {

        inventarioService.descontarStock(
                request.getIdInsumo(),
                request.getCantidad(),
                request.getIdUsuario(),
                request.getIdPedidoReferencia()
        );

        return ResponseEntity.ok("Stock descontado correctamente.");
    }
}