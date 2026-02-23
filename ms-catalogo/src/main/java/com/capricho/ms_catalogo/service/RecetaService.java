package com.capricho.ms_catalogo.service;

import com.capricho.ms_catalogo.dto.response.InsumoRequeridoDto;
import java.util.List;

public interface RecetaService {
    List<InsumoRequeridoDto> obtenerInsumosPorProducto(Long idProducto);
}