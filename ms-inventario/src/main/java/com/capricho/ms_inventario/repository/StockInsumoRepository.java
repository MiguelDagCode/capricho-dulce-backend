package com.capricho.ms_inventario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.capricho.ms_inventario.model.StockInsumo;

public interface StockInsumoRepository extends JpaRepository<StockInsumo, Long> {
}