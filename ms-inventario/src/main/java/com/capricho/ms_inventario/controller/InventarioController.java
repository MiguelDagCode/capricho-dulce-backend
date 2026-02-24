package com.capricho.ms_inventario.controller;

import com.capricho.ms_inventario.dto.request.ProcesarVentaRequest;
import com.capricho.ms_inventario.dto.request.RegistrarMermaRequest;
import com.capricho.ms_inventario.service.InventarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/inventario/operaciones")
@RequiredArgsConstructor
public class InventarioController {

    private final InventarioService inventarioService;

    @PostMapping("/procesar-venta")
    public ResponseEntity<Map<String, Object>> procesarVenta(@Valid @RequestBody ProcesarVentaRequest request) {
        
        inventarioService.procesarSalidaPorVenta(request);
        
        return ResponseEntity.ok(Map.of(
                "success", true,
                "mensaje", "Inventario descontado correctamente"
        ));
    }
    
    @PostMapping("/registrar-merma")
    public ResponseEntity<Map<String, Object>> registrarMerma(@Valid @RequestBody RegistrarMermaRequest request) {
        inventarioService.registrarMerma(request);
        
        return ResponseEntity.ok(Map.of(
                "success", true,
                "mensaje", "Merma registrada correctamente"
        ));
    }
}