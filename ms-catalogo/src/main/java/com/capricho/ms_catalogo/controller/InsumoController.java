package com.capricho.ms_catalogo.controller;

import com.capricho.ms_catalogo.dto.request.InsumoRequestDto;
import com.capricho.ms_catalogo.dto.response.InsumoResponseDto;
import com.capricho.ms_catalogo.service.InsumoService;
import com.capricho.ms_catalogo.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/catalogo/insumos")
@RequiredArgsConstructor
public class InsumoController {

    private final InsumoService insumoService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<InsumoResponseDto>>> listar() {

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Insumos listados correctamente",
                        LocalDateTime.now(),
                        insumoService.listarActivos())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InsumoResponseDto>> obtener(@PathVariable Long id) {

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Insumo encontrado",
                        LocalDateTime.now(),
                        insumoService.obtenerPorId(id))
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<InsumoResponseDto>> registrar(
            @Valid @RequestBody InsumoRequestDto request) {

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Insumo creado",
                        LocalDateTime.now(),
                        insumoService.registrar(request))
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<InsumoResponseDto>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody InsumoRequestDto request) {

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Insumo actualizado",
                        LocalDateTime.now(),
                        insumoService.actualizar(id, request))
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {

        insumoService.eliminar(id);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Insumo eliminado",
                        LocalDateTime.now(), null)
        );
    }
}