package com.capricho.ms_pedidos.util;

public class BusinessException extends RuntimeException {
	private static final long serialVersionUID = 1L;
    public BusinessException(String mensaje) {
        super(mensaje);
    }
}