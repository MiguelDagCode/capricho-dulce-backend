package com.capricho.ms_pedidos.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import com.capricho.ms_pedidos.enums.EstadoFactura;
import com.capricho.ms_pedidos.enums.TipoComprobante;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_facturacion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFactura;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pedido", nullable = false, unique = true)
    private Pedido pedido;

    @Column(nullable = false)
    private Long idUsuarioCajero;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoComprobante tipoComprobante;

    @Column(nullable = false, length = 4)
    private String serie;

    @Column(nullable = false, length = 8)
    private String correlativo;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime fechaEmision;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal montoSubtotal;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal montoIgv;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal montoTotal;

    @Column(length = 11)
    private String rucEmision;

    @Column(length = 150)
    private String razonSocialEmision;

    @Enumerated(EnumType.STRING)
    private EstadoFactura estado = EstadoFactura.EMITIDO;
}