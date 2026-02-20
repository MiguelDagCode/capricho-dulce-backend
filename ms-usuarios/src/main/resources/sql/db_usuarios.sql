DROP DATABASE IF EXISTS db_usuarios;
CREATE DATABASE db_usuarios;
USE db_usuarios;

CREATE TABLE tb_distrito (
    id_distrito INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    costo_envio DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    activo BOOLEAN DEFAULT TRUE
);

CREATE TABLE tb_rol (
    id_rol INT AUTO_INCREMENT PRIMARY KEY,
    nombre_rol VARCHAR(50) NOT NULL UNIQUE,
    activo BOOLEAN DEFAULT TRUE
);

CREATE TABLE tb_usuario (
    id_usuario BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    id_rol INT,
    activo BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (id_rol) REFERENCES tb_rol(id_rol)
);

CREATE TABLE tb_trabajador (
    id_usuario BIGINT PRIMARY KEY,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    celular VARCHAR(15) NOT NULL,
    tipo_documento ENUM('DNI', 'CE', 'PASAPORTE') NOT NULL,
    documento VARCHAR(20) NOT NULL UNIQUE,
    salario DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES tb_usuario(id_usuario)
);

CREATE TABLE tb_cliente (
    id_usuario BIGINT PRIMARY KEY,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    celular VARCHAR(15),
    direccion_referencia VARCHAR(255),
    id_distrito INT,
    tipo_documento ENUM('DNI', 'CE', 'RUC'),
    documento VARCHAR(20),
    FOREIGN KEY (id_usuario) REFERENCES tb_usuario(id_usuario),
    FOREIGN KEY (id_distrito) REFERENCES tb_distrito(id_distrito)
);
