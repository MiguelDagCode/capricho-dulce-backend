package com.capricho.ms_inventario.service.impl;

import com.capricho.ms_inventario.client.CatalogoClient;
import com.capricho.ms_inventario.dto.request.ItemProductoRequest;
import com.capricho.ms_inventario.dto.request.ProcesarVentaRequest;
import com.capricho.ms_inventario.dto.request.RegistrarMermaRequest;
import com.capricho.ms_inventario.dto.response.InsumoRequeridoDto;
import com.capricho.ms_inventario.enums.MotivoMovimiento;
import com.capricho.ms_inventario.enums.TipoMovimiento;
import com.capricho.ms_inventario.model.MovimientoInventario;
import com.capricho.ms_inventario.model.StockInsumo;
import com.capricho.ms_inventario.repository.MovimientoInventarioRepository;
import com.capricho.ms_inventario.repository.StockInsumoRepository;
import com.capricho.ms_inventario.service.InventarioService;
import com.capricho.ms_inventario.util.ApiResponse;
import com.capricho.ms_inventario.util.BusinessException;
import com.capricho.ms_inventario.util.ModeloNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventarioServiceImpl implements InventarioService {

    private final StockInsumoRepository stockInsumoRepository;
    private final MovimientoInventarioRepository movimientoRepository;
    private final CatalogoClient catalogoClient;

    @Override
    @Transactional // Un error aquí revierte absolutamente todos los inserts y updates
    public void procesarSalidaPorVenta(ProcesarVentaRequest request) {
        log.info("Procesando descuento de inventario para el Pedido ID: {}", request.getIdPedido());

        for (ItemProductoRequest item : request.getItems()) {
            
            // 1. Obtener receta del ms_catalogo
            ApiResponse<List<InsumoRequeridoDto>> response = catalogoClient.obtenerRecetaPorProducto(item.getIdProducto()).getBody();

            if (response == null || !response.isSuccess() || response.getData() == null || response.getData().isEmpty()) {
                throw new BusinessException("No se encontró receta activa para el producto ID: " + item.getIdProducto());
            }

            List<InsumoRequeridoDto> receta = response.getData();

            // 2. Iterar sobre los insumos necesarios
            for (InsumoRequeridoDto insumoReq : receta) {
                
                // Cálculo: CantidadBase (Ej: 0.500 kg) * CantidadPedida (Ej: 2 tortas) = 1.000 kg
                BigDecimal cantidadADescontar = insumoReq.getCantidadBase()
                        .multiply(new BigDecimal(item.getCantidad()));

                // 3. Buscar el insumo localmente
                StockInsumo stock = stockInsumoRepository.findById(insumoReq.getIdInsumo())
                        .orElseThrow(() -> new ModeloNotFoundException("El insumo ID " + insumoReq.getIdInsumo() + " no está registrado en el inventario."));

                // 4. Regla de Negocio: Validar Stock (Si no hay, abortamos todo lanzando error)
                if (stock.getStockActual().compareTo(cantidadADescontar) < 0) {
                    throw new BusinessException(String.format(
                        "Stock insuficiente de '%s'. Requerido: %s, Disponible: %s", 
                        insumoReq.getNombreInsumo(), cantidadADescontar, stock.getStockActual()
                    ));
                }

                // 5. Aplicar Descuento
                stock.setStockActual(stock.getStockActual().subtract(cantidadADescontar));
                
                // Regla de Negocio: Alerta de Stock Mínimo (No interrumpe la venta, solo avisa)
                if (stock.getStockActual().compareTo(stock.getStockMinimo()) <= 0) {
                    log.warn("⚠️ ALERTA DE STOCK: El insumo '{}' ha caído a su nivel mínimo o inferior ({}).", 
                            insumoReq.getNombreInsumo(), stock.getStockActual());
                }
                
                stockInsumoRepository.save(stock);

                // 6. Registrar Auditoría (Movimiento)
                MovimientoInventario movimiento = new MovimientoInventario();
                movimiento.setInsumo(stock);
                movimiento.setIdUsuario(request.getIdUsuario());
                movimiento.setIdPedidoReferencia(request.getIdPedido());
                movimiento.setTipoMovimiento(TipoMovimiento.SALIDA);
                movimiento.setMotivo(MotivoMovimiento.VENTA_PEDIDO);
                movimiento.setCantidad(cantidadADescontar);
                movimiento.setObservacion("Venta por Web. Producto ID: " + item.getIdProducto() + " (Cantidad: " + item.getCantidad() + ")");

                movimientoRepository.save(movimiento);
            }
        }
        log.info("✅ Inventario actualizado correctamente para el Pedido ID: {}", request.getIdPedido());
    }
    
    @Override
    @Transactional
    public void registrarMerma(RegistrarMermaRequest request) {
        log.info("Registrando merma para pedido ID: {}, producto: {}", request.getIdPedido(), request.getIdProducto());
        
        // Obtener receta del producto
        ApiResponse<List<InsumoRequeridoDto>> response = catalogoClient.obtenerRecetaPorProducto(request.getIdProducto()).getBody();
        
        if (response == null || !response.isSuccess() || response.getData() == null || response.getData().isEmpty()) {
            throw new BusinessException("No se encontró receta activa para el producto ID: " + request.getIdProducto());
        }
        
        List<InsumoRequeridoDto> receta = response.getData();
        
        for (InsumoRequeridoDto insumoReq : receta) {
            BigDecimal cantidadADescontar = insumoReq.getCantidadBase()
                    .multiply(new BigDecimal(request.getCantidad()));
            
            StockInsumo stock = stockInsumoRepository.findById(insumoReq.getIdInsumo())
                    .orElseThrow(() -> new ModeloNotFoundException("El insumo ID " + insumoReq.getIdInsumo() + " no está registrado en el inventario."));
            
            if (stock.getStockActual().compareTo(cantidadADescontar) < 0) {
                throw new BusinessException(String.format(
                    "Stock insuficiente para merma de '%s'. Requerido: %s, Disponible: %s", 
                    insumoReq.getNombreInsumo(), cantidadADescontar, stock.getStockActual()
                ));
            }
            
            stock.setStockActual(stock.getStockActual().subtract(cantidadADescontar));
            
            if (stock.getStockActual().compareTo(stock.getStockMinimo()) <= 0) {
                log.warn("⚠️ ALERTA DE STOCK POR MERMA: El insumo '{}' ha caído a su nivel mínimo ({}).", 
                        insumoReq.getNombreInsumo(), stock.getStockActual());
            }
            
            stockInsumoRepository.save(stock);
            
            MovimientoInventario movimiento = new MovimientoInventario();
            movimiento.setInsumo(stock);
            movimiento.setIdUsuario(request.getIdUsuario());
            movimiento.setIdPedidoReferencia(request.getIdPedido());
            movimiento.setTipoMovimiento(TipoMovimiento.SALIDA);
            movimiento.setMotivo(MotivoMovimiento.MERMA);
            movimiento.setCantidad(cantidadADescontar);
            movimiento.setObservacion("MERMA por error en preparación. Producto ID: " + request.getIdProducto() + 
                    ". Motivo ID: " + request.getIdMotivo() + 
                    (request.getObservacion() != null ? ". " + request.getObservacion() : ""));
            
            movimientoRepository.save(movimiento);
        }
        
        log.info("✅ Merma registrada correctamente para pedido ID: {}", request.getIdPedido());
    }
}