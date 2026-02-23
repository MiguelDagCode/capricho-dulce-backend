package com.capricho.ms_catalogo.service;

import com.capricho.ms_catalogo.dto.response.ProductoResponseDto;
import java.util.List;

public interface ProductoService {
    List<ProductoResponseDto> listarCatalogoActivo();
}