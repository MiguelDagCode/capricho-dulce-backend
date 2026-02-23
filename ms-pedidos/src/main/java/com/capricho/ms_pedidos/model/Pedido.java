package com.capricho.ms_pedidos.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import com.capricho.ms_pedidos.enums.EstadoPedido;
import com.capricho.ms_pedidos.enums.TipoEnvio;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_pedido")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPedido;

    // Referencias lógicas a otros microservicios
    @Column(nullable = false)
    private Long idUsuarioCliente;

    private Long idUsuarioPastelero;
    private Integer idDistrito;

    @Column(length = 255)
    private String direccionEntrega;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime fechaRegistro;

    private LocalDateTime fechaEntregaEstimada;
    private LocalDateTime fechaInicioCocina;
    private LocalDateTime fechaFinCocina;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPedido estadoPedido = EstadoPedido.PENDIENTE;

    @Enumerated(EnumType.STRING)
    private TipoEnvio tipoEnvio;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal montoSubtotal;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal montoEnvio = BigDecimal.ZERO;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal montoTotal;

    // Relación OneToMany con el Detalle (Bi-direccional)
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetallePedido> detalles = new ArrayList<>();

    // Relación OneToOne con la Facturación
    @OneToOne(mappedBy = "pedido", cascade = CascadeType.ALL)
    private Factura factura;

    // Método utilitario para agregar detalles y mantener la sincronización bi-direccional
    public void addDetalle(DetallePedido detalle) {
        detalles.add(detalle);
        detalle.setPedido(this);
    }
}