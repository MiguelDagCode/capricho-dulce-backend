package com.capricho.ms_catalogo.repository;

import com.capricho.ms_catalogo.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    // Busca productos activos (estado = true) cuya categoría también esté activa (activo = true)
    List<Producto> findByEstadoTrueAndCategoriaActivoTrue(); 
    Optional<Producto> findByNombre(String nombre);
}