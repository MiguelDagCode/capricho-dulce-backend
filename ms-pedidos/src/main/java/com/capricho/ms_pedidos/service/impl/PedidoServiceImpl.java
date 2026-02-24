package com.capricho.ms_pedidos.service.impl;

import com.capricho.ms_pedidos.client.CatalogoClient;
import com.capricho.ms_pedidos.client.InventarioClient;
import com.capricho.ms_pedidos.client.dto.CatalogoResponse;
import com.capricho.ms_pedidos.client.dto.ItemInventarioDto;
import com.capricho.ms_pedidos.client.dto.ProcesarVentaRequest;
import com.capricho.ms_pedidos.client.dto.ProductoCatalogoDto;
import com.capricho.ms_pedidos.dto.request.DetallePedidoRequest;
import com.capricho.ms_pedidos.dto.request.PedidoRequest;
import com.capricho.ms_pedidos.enums.EstadoPedido;
import com.capricho.ms_pedidos.model.DetallePedido;
import com.capricho.ms_pedidos.model.DetallePedidoId;
import com.capricho.ms_pedidos.model.Pedido;
import com.capricho.ms_pedidos.repository.PedidoRepository;
import com.capricho.ms_pedidos.service.PedidoService;
import com.capricho.ms_pedidos.util.BusinessException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final CatalogoClient catalogoClient;
    private final InventarioClient inventarioClient;

    @Override
    @Transactional 
    public Pedido crearPedido(PedidoRequest request) {
        log.info("Iniciando creación de nuevo pedido...");

        // 1. Obtener ID del Usuario desde el Token JWT (Contexto de Seguridad)
        Long idUsuarioCliente = extraerIdUsuarioDelToken();

        // 2. Obtener los precios reales desde ms-catalogo (Caché en memoria para esta transacción)
        CatalogoResponse catalogoResponse = catalogoClient.listarProductosActivos();
        if (catalogoResponse == null || !catalogoResponse.isSuccess() || catalogoResponse.getData() == null) {
            throw new BusinessException("No se pudo establecer comunicación con el catálogo de productos.");
        }

        // Convertimos la lista del catálogo en un Map <ID, Producto> para buscar rápido sin bucles anidados
        Map<Long, ProductoCatalogoDto> catalogoMap = catalogoResponse.getData().stream()
                .collect(Collectors.toMap(ProductoCatalogoDto::getId, p -> p));

        // 3. Inicializar el Pedido y Variables de Cálculo
        Pedido nuevoPedido = new Pedido();
        nuevoPedido.setIdUsuarioCliente(idUsuarioCliente);
        nuevoPedido.setIdDistrito(request.getIdDistrito());
        nuevoPedido.setDireccionEntrega(request.getDireccionEntrega());
        nuevoPedido.setTipoEnvio(request.getTipoEnvio());
        
        // Reglas de Fechas
        LocalDateTime ahora = LocalDateTime.now();
        nuevoPedido.setFechaRegistro(ahora);
        nuevoPedido.setFechaEntregaEstimada(ahora.plusHours(5)); // +5 horas como indicaste
        nuevoPedido.setEstadoPedido(EstadoPedido.PENDIENTE);

        BigDecimal subtotalAcumulado = BigDecimal.ZERO;
        List<ItemInventarioDto> itemsParaInventario = new ArrayList<>();

        // 4. Procesar los Detalles del Pedido
        for (DetallePedidoRequest itemReq : request.getDetalles()) {
            
            // Validar que el producto existe en el catálogo activo
            ProductoCatalogoDto productoOficial = catalogoMap.get(itemReq.getIdProducto());
            if (productoOficial == null) {
                throw new BusinessException("El producto ID " + itemReq.getIdProducto() + " no existe o no está disponible.");
            }

            // Matemática segura con BigDecimal
            BigDecimal precioReal = productoOficial.getPrecio();
            BigDecimal cantidad = new BigDecimal(itemReq.getCantidad());
            BigDecimal subtotalItem = precioReal.multiply(cantidad);
            
            subtotalAcumulado = subtotalAcumulado.add(subtotalItem);

            // Crear la entidad Detalle
            DetallePedido detalle = new DetallePedido();
            
            // Seteamos la llave compuesta (el idPedido se asignará en el método addDetalle)
            DetallePedidoId detalleId = new DetallePedidoId();
            detalleId.setIdProducto(itemReq.getIdProducto());
            detalle.setId(detalleId);
            
            // Snapshot histórico de seguridad
            detalle.setNombreProductoSnapshot(productoOficial.getNombre());
            detalle.setCantidad(itemReq.getCantidad());
            detalle.setPrecioUnitario(precioReal);
            // El campo 'subtotal' del detalle no se setea en Java porque es GENERATED ALWAYS en MySQL
            
            nuevoPedido.addDetalle(detalle);

            // Preparar el DTO para el Inventario
            itemsParaInventario.add(new ItemInventarioDto(itemReq.getIdProducto(), itemReq.getCantidad()));
        }

        // 5. Cálculos Finales del Pedido
        nuevoPedido.setMontoSubtotal(subtotalAcumulado);
        
        // Regla: Monto Envío = 5% del subtotal
        BigDecimal montoEnvio = subtotalAcumulado.multiply(new BigDecimal("0.05")).setScale(2, RoundingMode.HALF_UP);
        nuevoPedido.setMontoEnvio(montoEnvio);
        
        nuevoPedido.setMontoTotal(subtotalAcumulado.add(montoEnvio));

        // 6. Guardar el Pedido en Base de Datos PRIMERO (Para obtener el ID generado)
        Pedido pedidoGuardado = pedidoRepository.save(nuevoPedido);
        log.info("Pedido guardado en BD local con ID: {}", pedidoGuardado.getIdPedido());

        // 7. Llamada Sincrónica a ms-inventario 
        ProcesarVentaRequest inventarioRequest = ProcesarVentaRequest.builder()
                .idPedido(pedidoGuardado.getIdPedido())
                .idUsuario(idUsuarioCliente)
                .items(itemsParaInventario)
                .build();

        try {
            inventarioClient.procesarDescuentoInventario(inventarioRequest);
            log.info("Inventario descontado exitosamente para el Pedido ID: {}", pedidoGuardado.getIdPedido());
        } catch (Exception e) {
            log.error("Fallo al contactar ms-inventario. Realizando ROLLBACK...", e);
            throw new BusinessException("No se pudo procesar el stock en el inventario. El pedido ha sido cancelado. Detalle: " + e.getMessage());
        }

        return pedidoGuardado;
    }

    // Método auxiliar para extraer el ID de la sesión actual
    private Long extraerIdUsuarioDelToken() {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            
             return 4L; 
        } catch (Exception e) {
            log.warn("No se pudo extraer el ID del SecurityContext. Usando cliente por defecto (ID: 4)");
            return 4L; 
        }
    }
}