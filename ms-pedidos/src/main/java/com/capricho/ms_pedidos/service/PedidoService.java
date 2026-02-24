package com.capricho.ms_pedidos.service;

import com.capricho.ms_pedidos.dto.request.PedidoRequest;
import com.capricho.ms_pedidos.model.Pedido;

public interface PedidoService {

	public Pedido crearPedido(PedidoRequest request);
}
