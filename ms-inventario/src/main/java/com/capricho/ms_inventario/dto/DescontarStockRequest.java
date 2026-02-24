package com.capricho.ms_inventario.dto;

import java.math.BigDecimal;

public class DescontarStockRequest {

    private Long idInsumo;
    private BigDecimal cantidad;
    private Long idUsuario;
    private Long idPedidoReferencia;

    public Long getIdInsumo() {
        return idInsumo;
    }

    public void setIdInsumo(Long idInsumo) {
        this.idInsumo = idInsumo;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Long getIdPedidoReferencia() {
        return idPedidoReferencia;
    }

    public void setIdPedidoReferencia(Long idPedidoReferencia) {
        this.idPedidoReferencia = idPedidoReferencia;
    }
}