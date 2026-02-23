package com.capricho.ms_inventario.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.capricho.ms_inventario.dto.response.InsumoRequeridoDto;
import com.capricho.ms_inventario.util.ApiResponse;

import java.util.List;

@FeignClient(name = "ms-catalogo")
public interface CatalogoClient {

    @GetMapping("/api/catalogo/recetas/producto/{idProducto}")
    ResponseEntity<ApiResponse<List<InsumoRequeridoDto>>> obtenerRecetaPorProducto(@PathVariable("idProducto") Long idProducto);
    
}