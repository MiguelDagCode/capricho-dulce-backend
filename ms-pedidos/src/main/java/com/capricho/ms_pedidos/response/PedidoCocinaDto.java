package com.capricho.ms_pedidos.response;

import com.capricho.ms_pedidos.enums.EstadoPedido;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoCocinaDto {
    private Long idPedido;
    private EstadoPedido estado;
    private LocalDateTime fechaRegistro;
    private String tiempoTranscurrido; // "Hace 5 min"
    private List<DetalleCocinaDto> detalles;
}

