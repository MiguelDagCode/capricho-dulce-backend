package com.capricho.ms_pedidos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capricho.ms_pedidos.enums.EstadoPedido;
import com.capricho.ms_pedidos.model.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long>{
	List<Pedido> findByEstadoPedidoInOrderByFechaRegistroAsc(List<EstadoPedido> estados);
    
	List<Pedido> findByEstadoPedidoOrderByFechaRegistroAsc(EstadoPedido estado);
}
