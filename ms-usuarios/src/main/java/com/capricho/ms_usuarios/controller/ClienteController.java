package com.capricho.ms_usuarios.controller;

import com.capricho.ms_usuarios.dto.request.RegistroClienteRequest;
import com.capricho.ms_usuarios.dto.request.UpdatePerfilRequest;
import com.capricho.ms_usuarios.model.Cliente;
import com.capricho.ms_usuarios.service.ClienteService;
import com.capricho.ms_usuarios.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    // 1. Endpoint PÚBLICO (Cualquiera puede registrarse)
    @PostMapping("/registro")
    public ResponseEntity<ApiResponse<Cliente>> registrarse(@Valid @RequestBody RegistroClienteRequest request) {
        Cliente cliente = clienteService.registrarse(request);
        ApiResponse<Cliente> response = new ApiResponse<>(
                true, 
                "Cuenta creada exitosamente. ¡Bienvenido!", 
                LocalDateTime.now(), 
                cliente
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // 2. Endpoint PRIVADO (Solo el cliente logueado)
    @PutMapping("/mi-perfil")
    @PreAuthorize("hasRole('CLIENTE')") 
    public ResponseEntity<ApiResponse<Cliente>> actualizarMiPerfil(
            @Valid @RequestBody UpdatePerfilRequest request,
            Authentication authentication) { // <-- Spring inyecta al usuario actual aquí
            
        // Obtenemos el ID extraído de forma segura desde el Token JWT
        // (Dependiendo de tu JwtFilter, el Name/Principal suele ser el ID o el Correo. 
        // Si es correo, debes buscar el ID en el servicio. Si guardaste el ID en las Claims del token, sácalo de allí).
        // Asumiendo que configuraste tu UserDetails para retornar el ID o que lo puedes castear:
        
        // Forma genérica usando una utilidad hipotética o si guardaste el ID como Principal:
        Integer idUsuarioActual = extraerIdDesdeToken(authentication); 

        Cliente actualizado = clienteService.actualizarMiPerfil(idUsuarioActual, request);
        
        ApiResponse<Cliente> response = new ApiResponse<>(
                true, 
                "Perfil actualizado correctamente", 
                LocalDateTime.now(), 
                actualizado
        );
        return ResponseEntity.ok(response);
    }

    // 3. Endpoint PRIVADO (Desactivar propia cuenta)
    @PatchMapping("/mi-perfil/desactivar")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ApiResponse<Void>> desactivarMiCuenta(Authentication authentication) {
        
        Integer idUsuarioActual = extraerIdDesdeToken(authentication);
        clienteService.desactivarMiCuenta(idUsuarioActual);
        
        ApiResponse<Void> response = new ApiResponse<>(
                true, 
                "Tu cuenta ha sido desactivada. Lamentamos verte partir.", 
                LocalDateTime.now(), 
                null
        );
        return ResponseEntity.ok(response);
    }
    
    // --- Utilidad para sacar el ID (Ajústalo según cómo armes tu JWT) ---
    private Integer extraerIdDesdeToken(Authentication authentication) {

        // (Temporal para que compile): Reemplaza esto con tu lógica real de JWT
        return Integer.parseInt(authentication.getName()); 
    }
}