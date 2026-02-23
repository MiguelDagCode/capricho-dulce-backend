package com.capricho.ms_pedidos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capricho.ms_pedidos.model.Factura;

public interface FacturaRepository extends JpaRepository<Factura, Long>{

}
