package com.capricho.ms_catalogo.service;

import com.capricho.ms_catalogo.dto.request.InsumoRequestDto;
import com.capricho.ms_catalogo.dto.response.InsumoResponseDto;
import java.util.List;

public interface InsumoService {

    List<InsumoResponseDto> listarActivos();

    InsumoResponseDto obtenerPorId(Long id);

    InsumoResponseDto registrar(InsumoRequestDto request);

    InsumoResponseDto actualizar(Long id, InsumoRequestDto request);

    void eliminar(Long id);
}