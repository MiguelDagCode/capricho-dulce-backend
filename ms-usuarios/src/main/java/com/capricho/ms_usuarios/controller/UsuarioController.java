package com.capricho.ms_usuarios.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capricho.ms_usuarios.dto.request.TrabajadorCreateDto;
import com.capricho.ms_usuarios.dto.request.UsuarioUpdateDto;
import com.capricho.ms_usuarios.dto.response.UsuarioListResponseDto;
import com.capricho.ms_usuarios.model.Trabajador;
import com.capricho.ms_usuarios.service.UsuarioService;
import com.capricho.ms_usuarios.util.ApiResponse;
import com.capricho.ms_usuarios.util.ModeloNotFoundException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/usuarios")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class UsuarioController {
	private final UsuarioService usuarioService;
	
	@GetMapping
    public ResponseEntity<ApiResponse<List<UsuarioListResponseDto>>> findAll() {
        List<UsuarioListResponseDto> usuarios = usuarioService.findAll();
        ApiResponse<List<UsuarioListResponseDto>> response = new ApiResponse<>(
                true,
                "Lista de usuarios obtenida correctamente",
                LocalDateTime.now(),
                usuarios
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UsuarioListResponseDto>> findById(@PathVariable Long id) {
        UsuarioListResponseDto usuario = usuarioService.findById(id)
                .orElseThrow(() -> new ModeloNotFoundException("No se encontró el Usuario con ID: " + id));

        ApiResponse<UsuarioListResponseDto> response = new ApiResponse<>(
                true,
                "Usuario encontrado",
                LocalDateTime.now(),
                usuario
        );
        return ResponseEntity.ok(response);
    }


    @PatchMapping("/{id}/estado")
    public ResponseEntity<ApiResponse<Void>> deleteLogico(@PathVariable Long id) {
        usuarioService.deleteLogico(id);
        ApiResponse<Void> response = new ApiResponse<>(
                true,
                "Estado del usuario modificado con éxito",
                LocalDateTime.now(),
                null
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UsuarioListResponseDto>> update(
            @PathVariable Long id, 
            @Valid @RequestBody UsuarioUpdateDto request) {
            
        UsuarioListResponseDto usuarioActualizado = usuarioService.actualizar(id, request);
        ApiResponse<UsuarioListResponseDto> response = new ApiResponse<>(
                true,
                "Usuario actualizado con éxito",
                LocalDateTime.now(),
                usuarioActualizado
        );
        return ResponseEntity.ok(response);
    }


    @PostMapping("/trabajadores")
    public ResponseEntity<ApiResponse<Trabajador>> registrarTrabajador(@Valid @RequestBody TrabajadorCreateDto request) {
        Trabajador nuevoTrabajador = usuarioService.registrarTrabajador(request);
        
        ApiResponse<Trabajador> response = new ApiResponse<>(
                true, 
                "Trabajador registrado con éxito", 
                LocalDateTime.now(), 
                nuevoTrabajador
        );
        
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
