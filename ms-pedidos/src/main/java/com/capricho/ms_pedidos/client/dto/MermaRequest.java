package com.capricho.ms_pedidos.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MermaRequest {
    private Long idPedido;
    private Long idUsuario;
    private Long idProducto;
    private Integer cantidad;
    private Long idMotivo;
    private String observacion;
}