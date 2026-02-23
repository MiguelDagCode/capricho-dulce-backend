package com.capricho.ms_inventario.service;

import com.capricho.ms_inventario.dto.request.ProcesarVentaRequest;

public interface InventarioService {

	 void procesarSalidaPorVenta(ProcesarVentaRequest request);
}
