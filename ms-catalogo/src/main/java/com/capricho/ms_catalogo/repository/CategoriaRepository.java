package com.capricho.ms_catalogo.repository;

import com.capricho.ms_catalogo.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    // Solo listamos las categorías que no han sido eliminadas lógicamente
    List<Categoria> findByActivoTrue(); 
    boolean existsByNombre(String nombre);
}