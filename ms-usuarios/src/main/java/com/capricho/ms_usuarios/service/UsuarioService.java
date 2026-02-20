package com.capricho.ms_usuarios.service;

import com.capricho.ms_usuarios.model.Usuario;
import java.util.Optional;

public interface UsuarioService {
    
    // --- ENTIDAD (Uso Interno del Backend) ---
    
    // Lectura
    Optional<Usuario> buscarPorEmail(String email); // Reemplaza al obtenerPorEmail anterior
    
    // ... Aquí irán los demás métodos listarTodos, buscarPorId, etc.
    
    // Escritura
    // ... Aquí irán registrar, actualizar, cambiarEstado
    
    // --- PERSONALIZADAS (DTOs para los Controllers) ---
    // ... Aquí irán listarTodosDto, registrarDto, etc.
}