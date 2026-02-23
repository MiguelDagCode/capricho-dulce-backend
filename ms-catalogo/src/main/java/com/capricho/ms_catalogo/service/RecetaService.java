package com.capricho.ms_catalogo.service;

import com.capricho.ms_catalogo.dto.request.RecetaRequestDto;
import com.capricho.ms_catalogo.dto.response.RecetaResponseDto;

import java.util.List;

public interface RecetaService {

    RecetaResponseDto registrar(RecetaRequestDto request);

    List<RecetaResponseDto> listarPorProducto(Long idProducto);

    void eliminar(Long idProducto, Long idInsumo);
}