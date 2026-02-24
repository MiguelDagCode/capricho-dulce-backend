package com.capricho.ms_pedidos.client.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class ProcesarVentaRequest {
    private Long idPedido;
    private Long idUsuario;
    private List<ItemInventarioDto> items;
}