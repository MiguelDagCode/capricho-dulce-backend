package com.capricho.ms_pedidos.client;

import com.capricho.ms_pedidos.client.dto.ProcesarVentaRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "ms-inventario")
public interface InventarioClient {

    @PostMapping("/api/inventario/operaciones/procesar-venta")
    Map<String, Object> procesarDescuentoInventario(@RequestBody ProcesarVentaRequest request);
}