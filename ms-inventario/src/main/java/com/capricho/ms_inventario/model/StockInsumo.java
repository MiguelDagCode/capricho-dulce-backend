package com.capricho.ms_inventario.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_stock_insumo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockInsumo {

    @Id
    @Column(name = "id_insumo")
    private Long idInsumo; // Referencia Lógica exacta a ms-catalogo

    @Column(nullable = false, precision = 10, scale = 3)
    private BigDecimal stockActual = BigDecimal.ZERO;

    @Column(nullable = false, precision = 10, scale = 3)
    private BigDecimal stockMinimo = new BigDecimal("5.000");

    @UpdateTimestamp
    private LocalDateTime ultimaActualizacion;

    // Relación OneToMany para ver el historial de movimientos de este insumo
    @OneToMany(mappedBy = "insumo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MovimientoInventario> movimientos = new ArrayList<>();
}