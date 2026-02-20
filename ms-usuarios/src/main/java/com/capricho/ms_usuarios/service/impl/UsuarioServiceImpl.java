package com.capricho.ms_usuarios.service.impl;

import com.capricho.ms_usuarios.model.Usuario;
import com.capricho.ms_usuarios.repository.UsuarioRepository;
import com.capricho.ms_usuarios.service.UsuarioService;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor // Lombok inyecta automáticamente el UsuarioRepository
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Override   
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorEmail(String email) {
    	return usuarioRepository.findByEmail(email);
    }
   
}