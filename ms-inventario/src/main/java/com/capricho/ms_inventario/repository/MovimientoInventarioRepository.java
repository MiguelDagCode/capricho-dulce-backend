package com.capricho.ms_inventario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.capricho.ms_inventario.model.MovimientoInventario;

public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Long> {
}//