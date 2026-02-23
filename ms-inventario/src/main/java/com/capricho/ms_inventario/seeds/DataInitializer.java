package com.capricho.ms_inventario.seeds;

import com.capricho.ms_inventario.enums.MotivoMovimiento;
import com.capricho.ms_inventario.enums.TipoMovimiento;
import com.capricho.ms_inventario.model.MovimientoInventario;
import com.capricho.ms_inventario.model.StockInsumo;
import com.capricho.ms_inventario.repository.MovimientoInventarioRepository;
import com.capricho.ms_inventario.repository.StockInsumoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final StockInsumoRepository stockInsumoRepository;
    private final MovimientoInventarioRepository movimientoRepository;

    @Override
    @Transactional
    public void run(String... args) {
        log.info("--- INICIANDO CARGA DE STOCK INICIAL EN INVENTARIO ---");

        // Verificamos si ya hay stock para no duplicar ingresos cada vez que reiniciamos
        if (stockInsumoRepository.count() > 0) {
            log.info(" > El inventario ya tiene stock inicializado. Se omite la carga.");
            return;
        }

        // Definimos el ID del administrador que hace la compra inicial (Referencia a ms-usuarios)
        Long idAdminAlmacen = 1L;

        // IDs basados en el DataInitializer del ms-catalogo:
        // 1: Harina, 2: Azúcar, 3: Huevos, 4: Cacao, 5: Queso Crema, 6: Fresas

        // 1. Harina Pastelera (Cargamos 50 KG, mínimo 5 KG)
        inicializarInsumo(1L, new BigDecimal("50.000"), new BigDecimal("5.000"), idAdminAlmacen, "Compra inicial: Sacos de Harina");
        
        // 2. Azúcar Blanca (Cargamos 30 KG, mínimo 5 KG)
        inicializarInsumo(2L, new BigDecimal("30.000"), new BigDecimal("5.000"), idAdminAlmacen, "Compra inicial: Sacos de Azúcar");
        
        // 3. Huevos (Cargamos 200 Unidades, mínimo 30 Unidades)
        inicializarInsumo(3L, new BigDecimal("200.000"), new BigDecimal("30.000"), idAdminAlmacen, "Compra inicial: Planchas de Huevos");
        
        // 4. Cacao en Polvo (Cargamos 10 KG, mínimo 2 KG)
        inicializarInsumo(4L, new BigDecimal("10.000"), new BigDecimal("2.000"), idAdminAlmacen, "Compra inicial: Cajas de Cacao");
        
        // 5. Queso Crema (Cargamos 15 KG, mínimo 3 KG)
        inicializarInsumo(5L, new BigDecimal("15.000"), new BigDecimal("3.000"), idAdminAlmacen, "Compra inicial: Baldes de Queso Crema");
        
        // 6. Fresas Frescas (Cargamos 8 KG, mínimo 2 KG - perecible)
        inicializarInsumo(6L, new BigDecimal("8.000"), new BigDecimal("2.000"), idAdminAlmacen, "Compra inicial: Cajas de Fresas Frescas");

        log.info("--- CARGA DE STOCK INICIAL FINALIZADA ---");
    }

    private void inicializarInsumo(Long idInsumo, BigDecimal stockInicial, BigDecimal stockMinimo, Long idUsuario, String observacion) {
        
        // 1. Crear el registro en la tabla de Stock (Maestro)
        StockInsumo stock = new StockInsumo();
        stock.setIdInsumo(idInsumo); // Forzamos el ID para que coincida con el catálogo
        stock.setStockActual(stockInicial);
        stock.setStockMinimo(stockMinimo);
        
        stockInsumoRepository.save(stock);

        // 2. Registrar el movimiento de INGRESO en la Bitácora
        MovimientoInventario movimiento = new MovimientoInventario();
        movimiento.setInsumo(stock);
        movimiento.setIdUsuario(idUsuario);
        movimiento.setTipoMovimiento(TipoMovimiento.INGRESO);
        movimiento.setMotivo(MotivoMovimiento.COMPRA_MANUAL);
        movimiento.setCantidad(stockInicial);
        movimiento.setObservacion(observacion);
        
        movimientoRepository.save(movimiento);

        log.info(" > Insumo ID {} inicializado con {} de stock.", idInsumo, stockInicial);
    }
}