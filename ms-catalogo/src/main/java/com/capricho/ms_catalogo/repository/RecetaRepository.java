package com.capricho.ms_catalogo.repository;

import com.capricho.ms_catalogo.model.Receta;
import com.capricho.ms_catalogo.model.RecetaId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecetaRepository extends JpaRepository<Receta, RecetaId> {

    List<Receta> findByProductoIdProductoAndActivoTrue(Long idProducto);
}