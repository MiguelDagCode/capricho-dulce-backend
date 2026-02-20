package com.capricho.ms_catalogo.service.impl;

import com.capricho.ms_catalogo.dto.request.CategoriaRequestDto;
import com.capricho.ms_catalogo.dto.response.CategoriaResponseDto;
import com.capricho.ms_catalogo.model.Categoria;
import com.capricho.ms_catalogo.repository.CategoriaRepository;
import com.capricho.ms_catalogo.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaResponseDto> listarActivas() {
        return categoriaRepository.findByActivoTrue()
                .stream()
                .map(cat -> new CategoriaResponseDto(cat.getIdCategoria(), cat.getNombre()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CategoriaResponseDto registrar(CategoriaRequestDto request) {
        Categoria nuevaCategoria = new Categoria();
        nuevaCategoria.setNombre(request.getNombre());
        nuevaCategoria.setActivo(true);
        
        Categoria categoriaGuardada = categoriaRepository.save(nuevaCategoria);
        
        return new CategoriaResponseDto(categoriaGuardada.getIdCategoria(), categoriaGuardada.getNombre());
    }
}