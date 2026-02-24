package com.capricho.ms_inventario.service;

import com.capricho.ms_inventario.model.*;
import com.capricho.ms_inventario.repository.MovimientoInventarioRepository;
import com.capricho.ms_inventario.repository.StockInsumoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InventarioService {

    private final StockInsumoRepository stockRepository;
    private final MovimientoInventarioRepository movimientoRepository;

    /**
     * Endpoint especial que consumirá el microservicio de ventas.
     */
    @Transactional
    public void descontarStock(Long idInsumo,
                               BigDecimal cantidad,
                               Long idUsuario,
                               Long idPedidoReferencia) {

        StockInsumo stock = stockRepository.findById(idInsumo)
                .orElseThrow(() ->
                        new RuntimeException("No existe stock para el insumo: " + idInsumo));

        if (stock.getStockActual().compareTo(cantidad) < 0) {
            throw new RuntimeException("Stock insuficiente para el insumo: " + idInsumo);
        }

        // Restar stock
        stock.setStockActual(stock.getStockActual().subtract(cantidad));
        stock.setUltimaActualizacion(LocalDateTime.now());
        stockRepository.save(stock);

        // Registrar movimiento
        MovimientoInventario movimiento = new MovimientoInventario();
        movimiento.setIdInsumo(idInsumo);
        movimiento.setIdUsuario(idUsuario);
        movimiento.setTipoMovimiento(TipoMovimiento.SALIDA);
        movimiento.setMotivo(MotivoMovimiento.VENTA_PEDIDO);
        movimiento.setCantidad(cantidad);
        movimiento.setFechaMovimiento(LocalDateTime.now());
        movimiento.setIdPedidoReferencia(idPedidoReferencia);
        movimiento.setObservacion("Descuento por venta");

        movimientoRepository.save(movimiento);
    }


    @Transactional
    public void ingresarStock(Long idInsumo,
                              BigDecimal cantidad,
                              Long idUsuario) {

        StockInsumo stock = stockRepository.findById(idInsumo)
                .orElseThrow(() ->
                        new RuntimeException("No existe stock para el insumo: " + idInsumo));

        stock.setStockActual(stock.getStockActual().add(cantidad));
        stock.setUltimaActualizacion(LocalDateTime.now());
        stockRepository.save(stock);

        MovimientoInventario movimiento = new MovimientoInventario();
        movimiento.setIdInsumo(idInsumo);
        movimiento.setIdUsuario(idUsuario);
        movimiento.setTipoMovimiento(TipoMovimiento.INGRESO);
        movimiento.setMotivo(MotivoMovimiento.COMPRA_MANUAL);
        movimiento.setCantidad(cantidad);
        movimiento.setFechaMovimiento(LocalDateTime.now());
        movimiento.setObservacion("Ingreso manual");

        movimientoRepository.save(movimiento);
    }


    @Transactional
    public void registrarMerma(Long idInsumo,
                                BigDecimal cantidad,
                                Long idUsuario,
                                String observacion) {

        StockInsumo stock = stockRepository.findById(idInsumo)
                .orElseThrow(() ->
                        new RuntimeException("No existe stock para el insumo: " + idInsumo));

        if (stock.getStockActual().compareTo(cantidad) < 0) {
            throw new RuntimeException("Stock insuficiente para merma.");
        }

        stock.setStockActual(stock.getStockActual().subtract(cantidad));
        stock.setUltimaActualizacion(LocalDateTime.now());
        stockRepository.save(stock);

        MovimientoInventario movimiento = new MovimientoInventario();
        movimiento.setIdInsumo(idInsumo);
        movimiento.setIdUsuario(idUsuario);
        movimiento.setTipoMovimiento(TipoMovimiento.SALIDA);
        movimiento.setMotivo(MotivoMovimiento.MERMA);
        movimiento.setCantidad(cantidad);
        movimiento.setFechaMovimiento(LocalDateTime.now());
        movimiento.setObservacion(observacion);

        movimientoRepository.save(movimiento);
    }
}