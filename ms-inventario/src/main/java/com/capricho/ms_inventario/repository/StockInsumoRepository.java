package com.capricho.ms_inventario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capricho.ms_inventario.model.StockInsumo;

@Repository
public interface StockInsumoRepository extends JpaRepository<StockInsumo, Long> {

}
