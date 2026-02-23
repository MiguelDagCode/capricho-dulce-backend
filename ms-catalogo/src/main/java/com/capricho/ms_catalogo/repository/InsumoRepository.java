package com.capricho.ms_catalogo.repository;

import com.capricho.ms_catalogo.model.Insumo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InsumoRepository extends JpaRepository<Insumo, Long> {

    List<Insumo> findByActivoTrue();
}