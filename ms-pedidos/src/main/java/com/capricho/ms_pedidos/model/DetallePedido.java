package com.capricho.ms_pedidos.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "tb_detalle_pedido")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetallePedido {

    @EmbeddedId
    private DetallePedidoId id = new DetallePedidoId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idPedido") // Conecta la parte 'idPedido' de la llave compuesta con esta entidad
    @JoinColumn(name = "id_pedido")
    private Pedido pedido;

    @Column(nullable = false, length = 100)
    private String nombreProductoSnapshot;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    // Como es generada por BD, JPA solo la debe leer, nunca insertar ni actualizar
    @Column(insertable = false, updatable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Column(columnDefinition = "TEXT")
    private String observaciones;
}