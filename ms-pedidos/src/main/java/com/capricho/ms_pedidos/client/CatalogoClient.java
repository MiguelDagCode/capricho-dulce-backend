package com.capricho.ms_pedidos.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.capricho.ms_pedidos.client.dto.CatalogoResponse;

import java.util.Map;

@FeignClient(name = "ms-catalogo")
public interface CatalogoClient {

    @GetMapping("/api/catalogo/productos")
    CatalogoResponse listarProductosActivos();
}