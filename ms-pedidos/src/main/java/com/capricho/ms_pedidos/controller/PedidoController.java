package com.capricho.ms_pedidos.controller;

import com.capricho.ms_pedidos.dto.request.PedidoRequest;
import com.capricho.ms_pedidos.model.Pedido;
import com.capricho.ms_pedidos.response.PedidoCocinaDto;
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

import com.capricho.ms_pedidos.dto.request.RegistrarMermaRequest;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;



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
    
// ============= ENDPOINTS PARA COCINA (KDS) =============
    
    @GetMapping("/cocina/cola")
    @PreAuthorize("hasRole('PASTELERO') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> obtenerColaProduccion() {
        log.info("Pastelero solicitando cola de producción");
        
        List<PedidoCocinaDto> cola = pedidoService.obtenerColaProduccion();
        
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        response.put("mensaje", "Cola de producción obtenida exitosamente");
        response.put("fecha", LocalDateTime.now());
        response.put("data", cola);
        
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/cocina/{idPedido}/iniciar")
    @PreAuthorize("hasRole('PASTELERO') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> iniciarPreparacion(@PathVariable Long idPedido) {
        log.info("Pastelero iniciando preparación del pedido ID: {}", idPedido);
        
        Pedido pedido = pedidoService.iniciarPreparacion(idPedido);
        
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        response.put("mensaje", "Preparación iniciada exitosamente");
        response.put("fecha", LocalDateTime.now());
        
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("idPedido", pedido.getIdPedido());
        data.put("estado", pedido.getEstadoPedido());
        data.put("fechaInicioCocina", pedido.getFechaInicioCocina());
        
        response.put("data", data);
        
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/cocina/{idPedido}/listo")
    @PreAuthorize("hasRole('PASTELERO') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> marcarPedidoListo(@PathVariable Long idPedido) {
        log.info("Pastelero marcando pedido ID: {} como LISTO", idPedido);
        
        Pedido pedido = pedidoService.marcarPedidoListo(idPedido);
        
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        response.put("mensaje", "Pedido marcado como LISTO. Notificación enviada a despacho.");
        response.put("fecha", LocalDateTime.now());
        
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("idPedido", pedido.getIdPedido());
        data.put("estado", pedido.getEstadoPedido());
        data.put("fechaFinCocina", pedido.getFechaFinCocina());
        
        response.put("data", data);
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/cocina/{idPedido}/merma")
    @PreAuthorize("hasRole('PASTELERO') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> registrarMerma(
            @PathVariable Long idPedido,
            @Valid @RequestBody RegistrarMermaRequest request) {
        
        log.info("Pastelero registrando merma para pedido ID: {}, producto: {}", idPedido, request.getIdProducto());
        
        Pedido pedido = pedidoService.registrarMerma(idPedido, request);
        
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        response.put("mensaje", "Merma registrada exitosamente. El pedido ha sido reingresado a la cola.");
        response.put("fecha", LocalDateTime.now());
        
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("idPedido", pedido.getIdPedido());
        data.put("estado", pedido.getEstadoPedido());
        data.put("fechaRegistro", pedido.getFechaRegistro());
        
        response.put("data", data);
        
        return ResponseEntity.ok(response);
    }
}