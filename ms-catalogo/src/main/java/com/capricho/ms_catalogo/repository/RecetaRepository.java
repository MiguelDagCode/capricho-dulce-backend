package com.capricho.ms_catalogo.repository;

import com.capricho.ms_catalogo.model.Receta;
import com.capricho.ms_catalogo.model.RecetaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecetaRepository extends JpaRepository<Receta, RecetaId> {

    @Query("SELECT r FROM Receta r JOIN FETCH r.insumo " +
           "WHERE r.producto.idProducto = :idProducto AND r.activo = true")
    List<Receta> findRecetaActivaByProductoId(@Param("idProducto") Long idProducto);
}