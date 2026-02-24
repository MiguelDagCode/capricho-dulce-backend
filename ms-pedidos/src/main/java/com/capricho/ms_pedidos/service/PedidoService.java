package com.capricho.ms_pedidos.service;

import java.util.List;

import com.capricho.ms_pedidos.dto.request.PedidoRequest;
import com.capricho.ms_pedidos.dto.request.RegistrarMermaRequest;
import com.capricho.ms_pedidos.model.Pedido;
import com.capricho.ms_pedidos.response.PedidoCocinaDto;

public interface PedidoService {

	public Pedido crearPedido(PedidoRequest request);
	
	 // Métodos para cocina
    List<PedidoCocinaDto> obtenerColaProduccion();
    Pedido iniciarPreparacion(Long idPedido);
    Pedido marcarPedidoListo(Long idPedido);
    Pedido registrarMerma(Long idPedido, RegistrarMermaRequest request);
}
