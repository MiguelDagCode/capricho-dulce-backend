package com.capricho.ms_catalogo.service;

import com.capricho.ms_catalogo.dto.request.CategoriaRequestDto;
import com.capricho.ms_catalogo.dto.response.CategoriaResponseDto;

import java.util.List;

public interface CategoriaService {
    List<CategoriaResponseDto> listarActivas();
    CategoriaResponseDto registrar(CategoriaRequestDto request);
}