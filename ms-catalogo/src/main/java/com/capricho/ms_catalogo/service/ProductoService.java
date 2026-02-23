package com.capricho.ms_catalogo.service;

import com.capricho.ms_catalogo.dto.request.ProductoRequestDto;
import com.capricho.ms_catalogo.dto.response.ProductoResponseDto;

import java.util.List;

public interface ProductoService {

    List<ProductoResponseDto> listarActivos();

    ProductoResponseDto obtenerPorId(Long id);

    ProductoResponseDto registrar(ProductoRequestDto request);

    ProductoResponseDto actualizar(Long id, ProductoRequestDto request);

    void eliminar(Long id);
}