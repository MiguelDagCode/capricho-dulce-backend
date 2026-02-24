package com.capricho.ms_catalogo.service.impl;

import com.capricho.ms_catalogo.dto.response.InsumoRequeridoDto;
import com.capricho.ms_catalogo.repository.RecetaRepository;
import com.capricho.ms_catalogo.service.RecetaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecetaServiceImpl implements RecetaService {

    private final RecetaRepository recetaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<InsumoRequeridoDto> obtenerInsumosPorProducto(Long idProducto) {
        return recetaRepository.findRecetaActivaByProductoId(idProducto)
                .stream()
                .map(receta -> InsumoRequeridoDto.builder()
                        .idInsumo(receta.getInsumo().getIdInsumo())
                        .nombreInsumo(receta.getInsumo().getNombre())
                        .cantidadBase(receta.getCantidadInsumo())
                        .build())
                .collect(Collectors.toList());
    }
}