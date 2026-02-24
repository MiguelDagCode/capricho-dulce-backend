package com.capricho.ms_inventario.service;

import com.capricho.ms_inventario.dto.request.ProcesarVentaRequest;
import com.capricho.ms_inventario.dto.request.RegistrarMermaRequest;

public interface InventarioService {

	 void procesarSalidaPorVenta(ProcesarVentaRequest request);
	 void registrarMerma(RegistrarMermaRequest request);
}
