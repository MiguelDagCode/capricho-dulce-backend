package com.capricho.ms_catalogo.controller;

import com.capricho.ms_catalogo.dto.response.ProductoResponseDto;
import com.capricho.ms_catalogo.service.ProductoService;
import com.capricho.ms_catalogo.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/catalogo/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    // 🟢 ENDPOINT PÚBLICO
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductoResponseDto>>> listarProductos() {
        List<ProductoResponseDto> data = productoService.listarCatalogoActivo();
        
        ApiResponse<List<ProductoResponseDto>> response = new ApiResponse<>(
                true,
                "Catálogo de productos obtenido correctamente",
                LocalDateTime.now(),
                data
        );
        return ResponseEntity.ok(response);
    }
}