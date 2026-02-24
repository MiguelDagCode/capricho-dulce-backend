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
        log.info("--- INICIANDO CARGA DE STOCK INICIAL MASIVO EN INVENTARIO ---");

        if (stockInsumoRepository.count() > 0) {
            log.info(" > El inventario ya tiene stock inicializado. Se omite la carga.");
            return;
        }

        Long idAdminAlmacen = 1L;

        // ¡Stock aumentado drásticamente para aguantar la sustentación sin problemas!
        
        // 1. Harina Pastelera (De 50 a 500 KG)
        inicializarInsumo(1L, new BigDecimal("500.000"), new BigDecimal("10.000"), idAdminAlmacen, "Súper carga inicial: Harina");
        
        // 2. Azúcar Blanca (De 30 a 300 KG)
        inicializarInsumo(2L, new BigDecimal("300.000"), new BigDecimal("10.000"), idAdminAlmacen, "Súper carga inicial: Azúcar");
        
        // 3. Huevos (De 200 a 3000 Unidades)
        inicializarInsumo(3L, new BigDecimal("3000.000"), new BigDecimal("50.000"), idAdminAlmacen, "Súper carga inicial: Huevos");
        
        // 4. Cacao en Polvo (De 10 a 100 KG)
        inicializarInsumo(4L, new BigDecimal("100.000"), new BigDecimal("5.000"), idAdminAlmacen, "Súper carga inicial: Cacao");
        
        // 5. Queso Crema (De 15 a 150 KG)
        inicializarInsumo(5L, new BigDecimal("150.000"), new BigDecimal("5.000"), idAdminAlmacen, "Súper carga inicial: Queso Crema");
        
        // 6. Fresas Frescas (De 8 a 80 KG)
        inicializarInsumo(6L, new BigDecimal("80.000"), new BigDecimal("5.000"), idAdminAlmacen, "Súper carga inicial: Fresas");

        // --- RED DE SEGURIDAD (IDs 7 al 10) ---
        // Por si MySQL corrió los IDs durante las pruebas.
        inicializarInsumo(7L, new BigDecimal("200.000"), new BigDecimal("5.000"), idAdminAlmacen, "Stock de rescate ID 7");
        inicializarInsumo(8L, new BigDecimal("200.000"), new BigDecimal("5.000"), idAdminAlmacen, "Stock de rescate ID 8");
        inicializarInsumo(9L, new BigDecimal("200.000"), new BigDecimal("5.000"), idAdminAlmacen, "Stock de rescate ID 9");
        inicializarInsumo(10L, new BigDecimal("200.000"), new BigDecimal("5.000"), idAdminAlmacen, "Stock de rescate ID 10");

        log.info("--- CARGA DE STOCK MASIVO FINALIZADA ---");
    }

    private void inicializarInsumo(Long idInsumo, BigDecimal stockInicial, BigDecimal stockMinimo, Long idUsuario, String observacion) {
        
        StockInsumo stock = new StockInsumo();
        stock.setIdInsumo(idInsumo); 
        stock.setStockActual(stockInicial);
        stock.setStockMinimo(stockMinimo);
        
        stockInsumoRepository.save(stock);

        MovimientoInventario movimiento = new MovimientoInventario();
        movimiento.setInsumo(stock);
        movimiento.setIdUsuario(idUsuario);
        movimiento.setTipoMovimiento(TipoMovimiento.INGRESO);
        movimiento.setMotivo(MotivoMovimiento.COMPRA_MANUAL);
        movimiento.setCantidad(stockInicial);
        movimiento.setObservacion(observacion);
        
        movimientoRepository.save(movimiento);

        log.info(" > Insumo ID {} asegurado con {} unidades/kg.", idInsumo, stockInicial);
    }
}