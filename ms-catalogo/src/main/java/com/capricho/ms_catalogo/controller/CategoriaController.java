package com.capricho.ms_catalogo.controller;

import com.capricho.ms_catalogo.dto.request.CategoriaRequestDto;
import com.capricho.ms_catalogo.dto.response.CategoriaResponseDto;
import com.capricho.ms_catalogo.service.CategoriaService;
import com.capricho.ms_catalogo.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/catalogo/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;
    
    // 🟢 ENDPOINT PÚBLICO (Cualquier persona puede ver el catálogo)
    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoriaResponseDto>>> listarCategorias() {
        List<CategoriaResponseDto> data = categoriaService.listarActivas();
        
        ApiResponse<List<CategoriaResponseDto>> response = new ApiResponse<>(
                true,
                "Categorías obtenidas correctamente",
                LocalDateTime.now(),
                data
        );
        return ResponseEntity.ok(response);
    }

    // 🔴 ENDPOINT PROTEGIDO (Solo el Administrador puede crear categorías)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<CategoriaResponseDto>> registrarCategoria(@Valid @RequestBody CategoriaRequestDto request) {
        CategoriaResponseDto data = categoriaService.registrar(request);
        
        ApiResponse<CategoriaResponseDto> response = new ApiResponse<>(
                true,
                "Categoría creada con éxito",
                LocalDateTime.now(),
                data
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    // Obtener por ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoriaResponseDto>> obtenerPorId(@PathVariable Integer id) {

        CategoriaResponseDto data = categoriaService.obtenerPorId(id);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Categoría encontrada", LocalDateTime.now(), data)
        );
    }

    // Actualizar
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoriaResponseDto>> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody CategoriaRequestDto request) {

        CategoriaResponseDto data = categoriaService.actualizar(id, request);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Categoría actualizada", LocalDateTime.now(), data)
        );
    }

    // Eliminar (lógico)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Integer id) {

        categoriaService.eliminar(id);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Categoría eliminada correctamente", LocalDateTime.now(), null)
        );
    }
}