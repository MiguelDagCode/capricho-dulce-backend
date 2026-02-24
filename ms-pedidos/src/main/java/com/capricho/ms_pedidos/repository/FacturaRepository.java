package com.capricho.ms_pedidos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capricho.ms_pedidos.model.Factura;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long>{

}
