DROP DATABASE IF EXISTS db_catalogo;
CREATE DATABASE db_catalogo;
USE db_catalogo;

-- 1. CATEGORÍAS
CREATE TABLE tb_categoria (
    id_categoria INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    activo BOOLEAN DEFAULT TRUE
);

-- 2. PRODUCTOS (Lo que ve el cliente)
CREATE TABLE tb_producto (
    id_producto BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_categoria INT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT, -- Añadí descripción, vital para un catálogo web
    precio DECIMAL(10,2) NOT NULL,
    img_url VARCHAR(255),
    estado BOOLEAN DEFAULT TRUE, -- Soft Delete
    FOREIGN KEY (id_categoria) REFERENCES tb_categoria(id_categoria)
);

-- 3. INSUMOS MAESTROS (¡Sin stock!)
-- Solo nos importa saber qué existe y cómo se mide para armar la receta
CREATE TABLE tb_insumo (
    id_insumo BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL, -- Ej: 'Harina Blanca'
    unidad_medida VARCHAR(20) NOT NULL, -- KG, LT, UNIDAD
    activo BOOLEAN DEFAULT TRUE
);

-- 4. RECETAS (El puente entre Producto e Insumo)
CREATE TABLE tb_receta (
    id_producto BIGINT NOT NULL,
    id_insumo BIGINT NOT NULL,
    cantidad_insumo DECIMAL(10,3) NOT NULL, -- Ej: 0.500 (Medio Kilo)
    activo BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (id_producto, id_insumo),
    FOREIGN KEY (id_producto) REFERENCES tb_producto(id_producto),
    FOREIGN KEY (id_insumo) REFERENCES tb_insumo(id_insumo)
);

