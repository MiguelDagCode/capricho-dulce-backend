package com.capricho.ms_inventario.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import com.capricho.ms_inventario.enums.MotivoMovimiento;
import com.capricho.ms_inventario.enums.TipoMovimiento;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_movimiento_inventario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoInventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMovimiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_insumo", nullable = false)
    private StockInsumo insumo;

    @Column(nullable = false)
    private Long idUsuario; // Referencia a ms-usuarios (Quién hizo el movimiento)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoMovimiento tipoMovimiento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MotivoMovimiento motivo;

    @Column(nullable = false, precision = 10, scale = 3)
    private BigDecimal cantidad;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime fechaMovimiento;

    // Referencia al ms-pedidos (Opcional, solo se llena si es por una venta)
    private Long idPedidoReferencia;

    @Column(columnDefinition = "TEXT")
    private String observacion;
}