DROP DATABASE IF EXISTS db_inventario;
CREATE DATABASE db_inventario;
USE db_inventario;

-- =========================================================================
-- 1. CONTROL DE STOCK DIRECTO (Lo único que realmente importa)
-- =========================================================================
CREATE TABLE tb_stock_insumo (
    id_insumo BIGINT PRIMARY KEY, -- REFERENCIA LÓGICA a ms-catalogo (Mismo ID)
    stock_actual DECIMAL(10,3) NOT NULL DEFAULT 0.000,
    stock_minimo DECIMAL(10,3) NOT NULL DEFAULT 5.000,
    ultima_actualizacion DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- =========================================================================
-- 2. BITÁCORA DE MOVIMIENTOS (Reemplaza Compras, Notas, Mermas y Kardex)
-- =========================================================================
CREATE TABLE tb_movimiento_inventario (
    id_movimiento BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_insumo BIGINT NOT NULL,
    
    id_usuario BIGINT NOT NULL, 
    
    tipo_movimiento ENUM('INGRESO', 'SALIDA') NOT NULL,
    motivo ENUM('COMPRA_MANUAL', 'VENTA_PEDIDO', 'MERMA', 'AJUSTE') NOT NULL,
    
    cantidad DECIMAL(10,3) NOT NULL,
    fecha_movimiento DATETIME DEFAULT CURRENT_TIMESTAMP,
    
    -- Trazabilidad Simple
    id_pedido_referencia BIGINT NULL, -- REFERENCIA LÓGICA a ms-pedidos (Se llena si el motivo es VENTA_PEDIDO)
    observacion TEXT, -- Ej: "Compra en el mercado local" o "Se quemó en el horno"
    
    FOREIGN KEY (id_insumo) REFERENCES tb_stock_insumo(id_insumo)
);