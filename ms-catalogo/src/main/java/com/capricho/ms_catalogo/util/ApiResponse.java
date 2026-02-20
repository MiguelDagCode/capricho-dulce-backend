package com.capricho.ms_catalogo.util;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

	private boolean success;
	private String mensaje;
	private LocalDateTime fecha;
	private T data;
}
