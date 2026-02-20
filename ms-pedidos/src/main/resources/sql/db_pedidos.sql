DROP DATABASE IF EXISTS db_pedidos;
CREATE DATABASE db_pedidos;
USE db_pedidos;

-- 1. TABLA PEDIDO (Core)
CREATE TABLE tb_pedido (
    id_pedido BIGINT AUTO_INCREMENT PRIMARY KEY,
    
    -- REFERENCIAS LÓGICAS (No llevan FOREIGN KEY porque apuntan a ms-usuarios)
    id_usuario_cliente BIGINT NOT NULL, 
    id_usuario_pastelero BIGINT, -- Se llena cuando el pastelero toma el pedido
    id_distrito INT, -- Para auditoría de la zona de envío
    
    direccion_entrega VARCHAR(255), 
    
    -- TIEMPOS Y SLA
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_entrega_estimada DATETIME NULL,
    fecha_inicio_cocina DATETIME NULL,
    fecha_fin_cocina DATETIME NULL,
    
    -- ESTADOS
    estado_pedido ENUM('PENDIENTE', 'EN_PREPARACION', 'LISTO', 'ENTREGADO', 'CANCELADO') DEFAULT 'PENDIENTE',
    tipo_envio ENUM('DELIVERY', 'RECOJO_TIENDA'),
    
    -- TOTALES
    monto_subtotal DECIMAL(10,2) NOT NULL,
    monto_envio DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    monto_total DECIMAL(10,2) NOT NULL
);

-- 2. TABLA DETALLE PEDIDO
CREATE TABLE tb_detalle_pedido (
    id_pedido BIGINT NOT NULL,
    
    -- REFERENCIA LÓGICA (Apunta a ms-catalogo)
    id_producto BIGINT NOT NULL,
    
    -- SNAPSHOT DEL PRODUCTO (Inmutabilidad histórica)
    nombre_producto_snapshot VARCHAR(100) NOT NULL, 
    
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL, 
    subtotal DECIMAL(10,2) GENERATED ALWAYS AS (cantidad * precio_unitario) STORED, 
    observaciones TEXT,
    
    PRIMARY KEY (id_pedido, id_producto),
    FOREIGN KEY (id_pedido) REFERENCES tb_pedido(id_pedido) -- Esta FK sí va porque es interna
);

-- 3. TABLA FACTURACIÓN
CREATE TABLE tb_facturacion (
    id_factura BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_pedido BIGINT NOT NULL UNIQUE, 
    
    -- REFERENCIA LÓGICA (Apunta a ms-usuarios)
    id_usuario_cajero BIGINT NOT NULL, 
    
    -- DATOS FISCALES
    tipo_comprobante ENUM('BOLETA', 'FACTURA') NOT NULL,
    serie VARCHAR(4) NOT NULL, 
    correlativo VARCHAR(8) NOT NULL, 
    fecha_emision DATETIME DEFAULT CURRENT_TIMESTAMP,
    
    -- DATOS ECONÓMICOS
    monto_subtotal DECIMAL(10,2) NOT NULL, 
    monto_igv DECIMAL(10,2) NOT NULL, 
    monto_total DECIMAL(10,2) NOT NULL,
    
    -- DATOS DEL CLIENTE EN EL MOMENTO DE LA COMPRA (Snapshot)
    ruc_emision VARCHAR(11), 
    razon_social_emision VARCHAR(150),
    
    estado ENUM('EMITIDO', 'ANULADO') DEFAULT 'EMITIDO',
    
    FOREIGN KEY (id_pedido) REFERENCES tb_pedido(id_pedido) -- FK Interna
);