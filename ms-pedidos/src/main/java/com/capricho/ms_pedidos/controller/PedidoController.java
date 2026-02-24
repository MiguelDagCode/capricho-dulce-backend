package com.capricho.ms_pedidos.controller;

import com.capricho.ms_pedidos.dto.request.PedidoRequest;
import com.capricho.ms_pedidos.model.Pedido;
import com.capricho.ms_pedidos.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> registrarPedido(@Valid @RequestBody PedidoRequest request) {
        log.info("Recibiendo petición POST para registrar un nuevo pedido...");
        
        // Llamamos al servicio transaccional que orquesta todo
        Pedido pedidoCreado = pedidoService.crearPedido(request);
        
        // Armamos una respuesta limpia para no exponer las entidades crudas ni causar ciclos infinitos en Jackson
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        response.put("mensaje", "¡Pedido registrado exitosamente!");
        response.put("fecha", LocalDateTime.now());
        
        // Datos clave para el Frontend
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("idPedido", pedidoCreado.getIdPedido());
        data.put("estado", pedidoCreado.getEstadoPedido());
        data.put("fechaEntregaEstimada", pedidoCreado.getFechaEntregaEstimada());
        data.put("subtotal", pedidoCreado.getMontoSubtotal());
        data.put("costoEnvio", pedidoCreado.getMontoEnvio());
        data.put("total", pedidoCreado.getMontoTotal());
        
        response.put("data", data);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}