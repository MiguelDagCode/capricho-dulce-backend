package com.capricho.ms_pedidos.service.impl;

import com.capricho.ms_pedidos.client.CatalogoClient;
import com.capricho.ms_pedidos.client.InventarioClient;
import com.capricho.ms_pedidos.client.dto.*;
import com.capricho.ms_pedidos.dto.request.DetallePedidoRequest;
import com.capricho.ms_pedidos.dto.request.PedidoRequest;
import com.capricho.ms_pedidos.dto.request.RegistrarMermaRequest;

import com.capricho.ms_pedidos.enums.EstadoPedido;
import com.capricho.ms_pedidos.model.DetallePedido;
import com.capricho.ms_pedidos.model.DetallePedidoId;
import com.capricho.ms_pedidos.model.Pedido;
import com.capricho.ms_pedidos.repository.PedidoRepository;
import com.capricho.ms_pedidos.response.DetalleCocinaDto;
import com.capricho.ms_pedidos.response.PedidoCocinaDto;
import com.capricho.ms_pedidos.service.PedidoService;
import com.capricho.ms_pedidos.util.BusinessException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

        Long idUsuarioCliente = extraerIdUsuarioDelToken();

        CatalogoResponse catalogoResponse = catalogoClient.listarProductosActivos();
        if (catalogoResponse == null || !catalogoResponse.isSuccess() || catalogoResponse.getData() == null) {
            throw new BusinessException("No se pudo establecer comunicación con el catálogo de productos.");
        }

        Map<Long, ProductoCatalogoDto> catalogoMap = catalogoResponse.getData().stream()
                .collect(Collectors.toMap(ProductoCatalogoDto::getId, p -> p));

        Pedido nuevoPedido = new Pedido();
        nuevoPedido.setIdUsuarioCliente(idUsuarioCliente);
        nuevoPedido.setIdDistrito(request.getIdDistrito());
        nuevoPedido.setDireccionEntrega(request.getDireccionEntrega());
        nuevoPedido.setTipoEnvio(request.getTipoEnvio());
        
        LocalDateTime ahora = LocalDateTime.now();
        nuevoPedido.setFechaRegistro(ahora);
        nuevoPedido.setFechaEntregaEstimada(ahora.plusHours(5));
        nuevoPedido.setEstadoPedido(EstadoPedido.PENDIENTE);

        BigDecimal subtotalAcumulado = BigDecimal.ZERO;

        for (DetallePedidoRequest itemReq : request.getDetalles()) {
            
            ProductoCatalogoDto productoOficial = catalogoMap.get(itemReq.getIdProducto());
            if (productoOficial == null) {
                throw new BusinessException("El producto ID " + itemReq.getIdProducto() + " no existe o no está disponible.");
            }

            BigDecimal precioReal = productoOficial.getPrecio();
            BigDecimal cantidad = new BigDecimal(itemReq.getCantidad());
            BigDecimal subtotalItem = precioReal.multiply(cantidad);
            
            subtotalAcumulado = subtotalAcumulado.add(subtotalItem);

            DetallePedido detalle = new DetallePedido();
            
            DetallePedidoId detalleId = new DetallePedidoId();
            detalleId.setIdProducto(itemReq.getIdProducto());
            detalle.setId(detalleId);
            
            detalle.setNombreProductoSnapshot(productoOficial.getNombre());
            detalle.setCantidad(itemReq.getCantidad());
            detalle.setPrecioUnitario(precioReal);
            
            nuevoPedido.addDetalle(detalle);
        }

        nuevoPedido.setMontoSubtotal(subtotalAcumulado);
        
        BigDecimal montoEnvio = subtotalAcumulado.multiply(new BigDecimal("0.05")).setScale(2, RoundingMode.HALF_UP);
        nuevoPedido.setMontoEnvio(montoEnvio);
        
        nuevoPedido.setMontoTotal(subtotalAcumulado.add(montoEnvio));

        Pedido pedidoGuardado = pedidoRepository.save(nuevoPedido);
        log.info("Pedido guardado en BD local con ID: {}", pedidoGuardado.getIdPedido());

        // NOTA: Ya NO llamamos a inventario aquí. El descuento se hará cuando el pastelero marque como LISTO

        return pedidoGuardado;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoCocinaDto> obtenerColaProduccion() {
        log.info("Obteniendo cola de producción para cocina");
        
        // Obtenemos pedidos PENDIENTE y EN_PREPARACION ordenados por fecha_registro ASC (FIFO)
        List<Pedido> pedidos = pedidoRepository.findByEstadoPedidoInOrderByFechaRegistroAsc(
                List.of(EstadoPedido.PENDIENTE, EstadoPedido.EN_PREPARACION)
        );
        
        return pedidos.stream()
                .map(this::convertirAPedidoCocinaDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Pedido iniciarPreparacion(Long idPedido) {
        log.info("Iniciando preparación del pedido ID: {}", idPedido);
        
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new BusinessException("No se encontró el pedido con ID: " + idPedido));
        
        // Validar que el pedido esté en estado PENDIENTE
        if (pedido.getEstadoPedido() != EstadoPedido.PENDIENTE) {
            throw new BusinessException("Solo se pueden iniciar pedidos en estado PENDIENTE. Estado actual: " + pedido.getEstadoPedido());
        }
        
        Long idPastelero = extraerIdUsuarioDelToken();
        
        pedido.setEstadoPedido(EstadoPedido.EN_PREPARACION);
        pedido.setFechaInicioCocina(LocalDateTime.now());
        pedido.setIdUsuarioPastelero(idPastelero);
        
        return pedidoRepository.save(pedido);
    }

    @Override
    @Transactional
    public Pedido marcarPedidoListo(Long idPedido) {
        log.info("Marcando pedido ID: {} como LISTO", idPedido);
        
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new BusinessException("No se encontró el pedido con ID: " + idPedido));
        
        // Validar que el pedido esté en estado EN_PREPARACION
        if (pedido.getEstadoPedido() != EstadoPedido.EN_PREPARACION) {
            throw new BusinessException("Solo se pueden marcar como LISTO pedidos en preparación. Estado actual: " + pedido.getEstadoPedido());
        }
        
        pedido.setEstadoPedido(EstadoPedido.LISTO);
        pedido.setFechaFinCocina(LocalDateTime.now());
        
        Pedido pedidoGuardado = pedidoRepository.save(pedido);
        
        // AHORA descontamos el inventario (Opción A recomendada)
        try {
            List<ItemInventarioDto> itemsParaInventario = pedido.getDetalles().stream()
                    .map(detalle -> new ItemInventarioDto(
                            detalle.getId().getIdProducto(), 
                            detalle.getCantidad()))
                    .collect(Collectors.toList());
            
            ProcesarVentaRequest inventarioRequest = ProcesarVentaRequest.builder()
                    .idPedido(pedido.getIdPedido())
                    .idUsuario(pedido.getIdUsuarioPastelero()) // El pastelero que lo terminó
                    .items(itemsParaInventario)
                    .build();
            
            inventarioClient.procesarDescuentoInventario(inventarioRequest);
            log.info("Inventario descontado exitosamente para el Pedido ID: {}", pedido.getIdPedido());
            
        } catch (Exception e) {
            log.error("Error al descontar inventario para pedido ID: {}. El pedido ya está LISTO pero el inventario no se actualizó. Se requiere intervención manual.", idPedido, e);
            // No lanzamos excepción para no revertir el cambio de estado del pedido
            // Pero deberíamos tener un mecanismo de compensación (ej. cola de reintentos)
        }
        
        return pedidoGuardado;
    }

    @Override
    @Transactional
    public Pedido registrarMerma(Long idPedido, RegistrarMermaRequest request) {
        log.info("Registrando merma para pedido ID: {}, producto: {}", idPedido, request.getIdProducto());
        
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new BusinessException("No se encontró el pedido con ID: " + idPedido));
        
        // Validar que el pedido esté en estado EN_PREPARACION
        if (pedido.getEstadoPedido() != EstadoPedido.EN_PREPARACION) {
            throw new BusinessException("Solo se pueden registrar mermas en pedidos en preparación. Estado actual: " + pedido.getEstadoPedido());
        }
        
        // Buscar el detalle específico
        DetallePedido detalleMermado = pedido.getDetalles().stream()
                .filter(d -> d.getId().getIdProducto().equals(request.getIdProducto()))
                .findFirst()
                .orElseThrow(() -> new BusinessException("El producto ID " + request.getIdProducto() + " no pertenece a este pedido"));
        
        Long idPastelero = extraerIdUsuarioDelToken();
        
        // Registrar la merma en inventario (descuento por merma)
        try {
            MermaRequest inventarioRequest = MermaRequest.builder()
                .idPedido(idPedido)
                .idUsuario(idPastelero)
                .idProducto(request.getIdProducto())
                .cantidad(detalleMermado.getCantidad())
                .idMotivo(request.getIdMotivo())
                .observacion(request.getObservacion())
                .build();
            
            inventarioClient.registrarMerma(inventarioRequest);
            log.info("Merma registrada en inventario para pedido ID: {}, producto: {}", idPedido, request.getIdProducto());
            
        } catch (Exception e) {
            log.error("Error al registrar merma en inventario. No se puede continuar.", e);
            throw new BusinessException("No se pudo registrar la merma en el sistema de inventario. Intente nuevamente.");
        }
        
        // Si el pedido tiene un solo detalle, reiniciamos todo el pedido
        if (pedido.getDetalles().size() == 1) {
            pedido.setEstadoPedido(EstadoPedido.PENDIENTE);
            pedido.setFechaRegistro(LocalDateTime.now()); // Reiniciamos el timer para que vuelva a ser FIFO
            pedido.setFechaInicioCocina(null);
            pedido.setIdUsuarioPastelero(null);
            log.info("Pedido reiniciado a PENDIENTE por merma del único producto");
        } else {
            // Si hay múltiples productos, la lógica se complica
            // Para simplificar, asumimos que se merma todo el pedido o se maneja por separado
            // En un sistema real, podrías tener una tabla de "intentos de preparación"
            throw new BusinessException("La gestión de mermas parciales aún no está implementada. Por favor, cancele y cree un nuevo pedido.");
        }
        
        return pedidoRepository.save(pedido);
    }

    // ============= MÉTODOS AUXILIARES =============
    
    private PedidoCocinaDto convertirAPedidoCocinaDto(Pedido pedido) {
        List<DetalleCocinaDto> detallesDto = pedido.getDetalles().stream()
                .map(detalle -> DetalleCocinaDto.builder()
                        .idProducto(detalle.getId().getIdProducto())
                        .nombreProducto(detalle.getNombreProductoSnapshot())
                        .cantidad(detalle.getCantidad())
                        .observaciones(detalle.getObservaciones())
                        .precioUnitario(detalle.getPrecioUnitario())
                        .build())
                .collect(Collectors.toList());
        
        return PedidoCocinaDto.builder()
                .idPedido(pedido.getIdPedido())
                .estado(pedido.getEstadoPedido())
                .fechaRegistro(pedido.getFechaRegistro())
                .tiempoTranscurrido(calcularTiempoTranscurrido(pedido.getFechaRegistro()))
                .detalles(detallesDto)
                .build();
    }
    
    private String calcularTiempoTranscurrido(LocalDateTime fechaRegistro) {
        if (fechaRegistro == null) return "Desconocido";
        
        Duration duration = Duration.between(fechaRegistro, LocalDateTime.now());
        long minutos = duration.toMinutes();
        
        if (minutos < 1) {
            return "Hace unos segundos";
        } else if (minutos < 60) {
            return "Hace " + minutos + " min" + (minutos == 1 ? "" : "s");
        } else {
            long horas = minutos / 60;
            long minsRestantes = minutos % 60;
            return "Hace " + horas + "h " + minsRestantes + "m";
        }
    }
    
    private Long extraerIdUsuarioDelToken() {
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            // Aquí deberías extraer el ID real del token
            // Por ahora retornamos un valor por defecto
            return 4L; 
        } catch (Exception e) {
            log.warn("No se pudo extraer el ID del SecurityContext. Usando cliente por defecto (ID: 4)");
            return 4L; 
        }
    }
}