package com.capricho.ms_catalogo.repository;

import com.capricho.ms_catalogo.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    // Solo listamos las categorías que no han sido eliminadas lógicamente
    List<Categoria> findByActivoTrue(); 
    Optional<Categoria> findByNombre(String nombre);
}