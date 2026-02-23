package com.capricho.ms_catalogo.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.capricho.ms_catalogo.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long>{
	List<Producto> findByEstadoTrue();
}
