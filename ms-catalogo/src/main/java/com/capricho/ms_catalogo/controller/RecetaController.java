package com.capricho.ms_catalogo.controller;

import com.capricho.ms_catalogo.dto.response.InsumoRequeridoDto;
import com.capricho.ms_catalogo.service.RecetaService;
import com.capricho.ms_catalogo.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/catalogo/recetas")
@RequiredArgsConstructor
public class RecetaController {

    private final RecetaService recetaService;

    // Endpoint interno que consumirá ms-inventario vía OpenFeign
    @GetMapping("/producto/{idProducto}")
    public ResponseEntity<ApiResponse<List<InsumoRequeridoDto>>> obtenerRecetaPorProducto(@PathVariable Long idProducto) {
        
        List<InsumoRequeridoDto> insumos = recetaService.obtenerInsumosPorProducto(idProducto);
        
        ApiResponse<List<InsumoRequeridoDto>> response = new ApiResponse<>(
                true,
                "Receta obtenida correctamente",
                LocalDateTime.now(),
                insumos
        );
        
        return ResponseEntity.ok(response);
    }
}