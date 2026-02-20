package com.capricho.ms_usuarios.service;

import com.capricho.ms_usuarios.dto.request.TrabajadorCreateDto;
import com.capricho.ms_usuarios.dto.request.UsuarioUpdateDto;
import com.capricho.ms_usuarios.dto.response.UsuarioListResponseDto;
import com.capricho.ms_usuarios.model.Trabajador;
import com.capricho.ms_usuarios.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    
    // --- ENTIDAD (Uso Interno del Backend) ---
    
    // Lectura
    Optional<Usuario> buscarPorEmail(String email); 
    
    // Escritura
    
    // --- PERSONALIZADAS (DTOs para los Controllers) ---
    // Lectura 
    List<UsuarioListResponseDto> findAll();
    Optional<UsuarioListResponseDto> findById(Long idUsuario);
    
    // Escritura
    void deleteLogico(Long idUsuario);
    UsuarioListResponseDto actualizar(Long idUsuario, UsuarioUpdateDto request);
    Trabajador registrarTrabajador(TrabajadorCreateDto request);
}