package com.capricho.ms_usuarios.repository;

import com.capricho.ms_usuarios.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    // Spring Data JPA construye la consulta SQL automáticamente basándose en el nombre del método
    Optional<Usuario> findByEmail(String email);
    
    // Un método extra que te servirá mucho después para validaciones al registrar usuarios
    boolean existsByEmail(String email);
}