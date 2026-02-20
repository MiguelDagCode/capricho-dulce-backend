DROP DATABASE IF EXISTS db_inventario;
CREATE DATABASE db_inventario;
USE db_inventario;

-- =========================================================================
-- 1. CONTROL DE STOCK (La tabla principal de este MS)
-- =========================================================================
CREATE TABLE tb_stock_insumo (
    id_insumo BIGINT PRIMARY KEY, -- REFERENCIA LÓGICA a ms-catalogo (Mismo ID)
    stock_actual DECIMAL(10,3) NOT NULL DEFAULT 0.000,
    stock_minimo DECIMAL(10,3) NOT NULL DEFAULT 5.000,
    ultima_actualizacion DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- =========================================================================
-- 2. MÓDULO DE PROVEEDORES Y COMPRAS
-- =========================================================================
CREATE TABLE tb_proveedor (
    id_proveedor INT AUTO_INCREMENT PRIMARY KEY,
    ruc VARCHAR(11) NOT NULL UNIQUE,
    razon_social VARCHAR(150) NOT NULL,
    nombre_contacto VARCHAR(100),
    celular_contacto VARCHAR(15),
    email VARCHAR(100),
    activo BOOLEAN DEFAULT TRUE
);

CREATE TABLE tb_orden_compra (
    id_orden_compra BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_proveedor INT NOT NULL,
    
    id_usuario_admin BIGINT NOT NULL, -- REFERENCIA LÓGICA a ms-usuarios (Quién compra)
    
    fecha_emision DATETIME DEFAULT CURRENT_TIMESTAMP,
    fecha_entrega_esperada DATETIME NULL,
    estado ENUM('GENERADA', 'ENVIADA', 'RECIBIDA_PARCIAL', 'RECIBIDA_TOTAL', 'CANCELADA') DEFAULT 'GENERADA',
    monto_total DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    observaciones TEXT,
    
    FOREIGN KEY (id_proveedor) REFERENCES tb_proveedor(id_proveedor)
);

CREATE TABLE tb_detalle_orden_compra (
    id_orden_compra BIGINT NOT NULL,
    id_insumo BIGINT NOT NULL,
    cantidad DECIMAL(10,3) NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) GENERATED ALWAYS AS (cantidad * precio_unitario) STORED,
    
    PRIMARY KEY (id_orden_compra, id_insumo),
    FOREIGN KEY (id_orden_compra) REFERENCES tb_orden_compra(id_orden_compra),
    FOREIGN KEY (id_insumo) REFERENCES tb_stock_insumo(id_insumo) -- FK INTERNA
);

-- =========================================================================
-- 3. MÓDULO DE RECEPCIÓN (Mueve el stock)
-- =========================================================================
CREATE TABLE tb_nota_ingreso (
    id_nota_ingreso BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_orden_compra BIGINT NULL, 
    
    id_usuario_almacen BIGINT NOT NULL, -- REFERENCIA LÓGICA a ms-usuarios (Quién recibe)
    
    fecha_ingreso DATETIME DEFAULT CURRENT_TIMESTAMP,
    tipo_comprobante ENUM('GUIA_REMISION', 'FACTURA', 'BOLETA', 'TICKET_INTERNO') NOT NULL,
    numero_comprobante VARCHAR(50) NOT NULL,
    estado ENUM('COMPLETADO', 'ANULADO') DEFAULT 'COMPLETADO',
    
    FOREIGN KEY (id_orden_compra) REFERENCES tb_orden_compra(id_orden_compra)
);

CREATE TABLE tb_detalle_nota_ingreso (
    id_nota_ingreso BIGINT NOT NULL,
    id_insumo BIGINT NOT NULL,
    cantidad_recibida DECIMAL(10,3) NOT NULL,
    precio_compra DECIMAL(10,2) NOT NULL, 
    
    PRIMARY KEY (id_nota_ingreso, id_insumo),
    FOREIGN KEY (id_nota_ingreso) REFERENCES tb_nota_ingreso(id_nota_ingreso),
    FOREIGN KEY (id_insumo) REFERENCES tb_stock_insumo(id_insumo) -- FK INTERNA
);

-- =========================================================================
-- 4. MERMAS (Pérdidas de inventario)
-- =========================================================================
CREATE TABLE tb_merma (
    id_merma BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha_reporte DATETIME DEFAULT CURRENT_TIMESTAMP,
    
    id_usuario_reporta BIGINT NOT NULL, -- REFERENCIA LÓGICA a ms-usuarios
    id_producto BIGINT NULL,            -- REFERENCIA LÓGICA a ms-catalogo (Si se pierde torta lista)
    id_insumo BIGINT NULL,              -- FK INTERNA (Si se pierde materia prima)
    
    cantidad DECIMAL(10,3) NOT NULL,
    motivo ENUM('VENCIDO', 'MALA_MANIPULACION', 'FALLA_HORNO', 'PRUEBA_CALIDAD', 'ROBO') NOT NULL,
    observacion TEXT,
    
    FOREIGN KEY (id_insumo) REFERENCES tb_stock_insumo(id_insumo),
    
    CONSTRAINT check_origen_merma CHECK (
        (id_insumo IS NOT NULL AND id_producto IS NULL) OR 
        (id_insumo IS NULL AND id_producto IS NOT NULL)
    )
);

-- =========================================================================
-- 5. KARDEX (Auditoría inmutable)
-- =========================================================================
CREATE TABLE tb_kardex (
    id_kardex BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_insumo BIGINT NOT NULL,
    fecha_movimiento DATETIME DEFAULT CURRENT_TIMESTAMP,
    
    tipo_movimiento ENUM('INGRESO', 'SALIDA') NOT NULL,
    motivo ENUM('COMPRA', 'CONSUMO_VENTA', 'MERMA', 'AJUSTE_INVENTARIO') NOT NULL,
    
    cantidad DECIMAL(10,3) NOT NULL,
    saldo_actual DECIMAL(10,3) NOT NULL, 
    
    -- Trazabilidad (Solo 1 estará lleno dependiendo del motivo)
    id_nota_ingreso BIGINT NULL, -- FK INTERNA (Si fue por COMPRA)
    id_merma BIGINT NULL,        -- FK INTERNA (Si fue por MERMA)
    id_pedido BIGINT NULL,       -- REFERENCIA LÓGICA a ms-pedidos (Si fue por CONSUMO_VENTA)
    
    observacion TEXT,
    
    FOREIGN KEY (id_insumo) REFERENCES tb_stock_insumo(id_insumo),
    FOREIGN KEY (id_nota_ingreso) REFERENCES tb_nota_ingreso(id_nota_ingreso),
    FOREIGN KEY (id_merma) REFERENCES tb_merma(id_merma)
);