package com.capricho.ms_catalogo.controller;

import com.capricho.ms_catalogo.dto.request.ProductoRequestDto;
import com.capricho.ms_catalogo.dto.response.ProductoResponseDto;
import com.capricho.ms_catalogo.service.ProductoService;
import com.capricho.ms_catalogo.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/catalogo/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductoResponseDto>>> listar() {

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Productos listados correctamente",
                        LocalDateTime.now(),
                        productoService.listarActivos())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductoResponseDto>> obtener(@PathVariable Long id) {

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Producto encontrado",
                        LocalDateTime.now(),
                        productoService.obtenerPorId(id))
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<ProductoResponseDto>> registrar(
            @Valid @RequestBody ProductoRequestDto request) {

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Producto creado",
                        LocalDateTime.now(),
                        productoService.registrar(request))
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductoResponseDto>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProductoRequestDto request) {

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Producto actualizado",
                        LocalDateTime.now(),
                        productoService.actualizar(id, request))
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {

        productoService.eliminar(id);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Producto eliminado",
                        LocalDateTime.now(), null)
        );
    }
}