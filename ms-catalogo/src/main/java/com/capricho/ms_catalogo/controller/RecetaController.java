package com.capricho.ms_catalogo.controller;

import com.capricho.ms_catalogo.dto.request.RecetaRequestDto;
import com.capricho.ms_catalogo.dto.response.RecetaResponseDto;
import com.capricho.ms_catalogo.service.RecetaService;
import com.capricho.ms_catalogo.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/catalogo/recetas")
@RequiredArgsConstructor
public class RecetaController {

    private final RecetaService recetaService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<RecetaResponseDto>> registrar(
            @Valid @RequestBody RecetaRequestDto request) {

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Insumo agregado a receta",
                        LocalDateTime.now(),
                        recetaService.registrar(request))
        );
    }

    @GetMapping("/producto/{idProducto}")
    public ResponseEntity<ApiResponse<List<RecetaResponseDto>>> listar(
            @PathVariable Long idProducto) {

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Receta listada",
                        LocalDateTime.now(),
                        recetaService.listarPorProducto(idProducto))
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{idProducto}/{idInsumo}")
    public ResponseEntity<ApiResponse<Void>> eliminar(
            @PathVariable Long idProducto,
            @PathVariable Long idInsumo) {

        recetaService.eliminar(idProducto, idInsumo);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Receta eliminada",
                        LocalDateTime.now(), null)
        );
    }
}