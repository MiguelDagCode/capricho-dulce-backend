package com.capricho.ms_usuarios.controller;

import com.capricho.ms_usuarios.model.Rol;
import com.capricho.ms_usuarios.service.RolService;
import com.capricho.ms_usuarios.util.ApiResponse;
import com.capricho.ms_usuarios.util.ModeloNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin/roles")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')") // Proteger a nivel de controlador
public class RolController {

    private final RolService rolService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Rol>>> listarTodos() {
        List<Rol> roles = rolService.listarTodos();
        ApiResponse<List<Rol>> response = new ApiResponse<>(
                true, 
                "Lista de roles obtenida correctamente", 
                LocalDateTime.now(), 
                roles
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Rol>> buscarPorId(@PathVariable Integer id) {
        Rol rol = rolService.buscarPorId(id)
                .orElseThrow(() -> new ModeloNotFoundException("No se encontró el Rol con ID: " + id));
        
        ApiResponse<Rol> response = new ApiResponse<>(
                true, 
                "Rol encontrado", 
                LocalDateTime.now(), 
                rol
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Rol>> registrarRol(@Valid @RequestBody Rol rol) {
        Rol nuevoRol = rolService.registrarRol(rol);
        ApiResponse<Rol> response = new ApiResponse<>(
                true, 
                "Rol creado con éxito", 
                LocalDateTime.now(), 
                nuevoRol
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Rol>> actualizarRol(@PathVariable Integer id, @Valid @RequestBody Rol rol) {
        Rol rolActualizado = rolService.actualizarRol(id, rol);
        ApiResponse<Rol> response = new ApiResponse<>(
                true, 
                "Rol actualizado con éxito", 
                LocalDateTime.now(), 
                rolActualizado
        );
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<ApiResponse<Void>> cambiarEstado(@PathVariable Integer id) {
        rolService.cambiarEstado(id);
        ApiResponse<Void> response = new ApiResponse<>(
                true, 
                "Estado del rol modificado con éxito", 
                LocalDateTime.now(), 
                null
        );
        return ResponseEntity.ok(response);
    }
}