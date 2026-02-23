package com.capricho.ms_catalogo.service.impl;

import com.capricho.ms_catalogo.dto.request.CategoriaRequestDto;
import com.capricho.ms_catalogo.dto.response.CategoriaResponseDto;
import com.capricho.ms_catalogo.model.Categoria;
import com.capricho.ms_catalogo.repository.CategoriaRepository;
import com.capricho.ms_catalogo.service.CategoriaService;
import com.capricho.ms_catalogo.util.ModeloNotFoundException;

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

	@Override
	@Transactional(readOnly = true)
	public CategoriaResponseDto obtenerPorId(Integer id) {
		Categoria categoria = categoriaRepository.findById(id)
				.orElseThrow(() -> new ModeloNotFoundException("Categoria no encontrada"));
		
		return new CategoriaResponseDto(
				categoria.getIdCategoria(),
				categoria.getNombre()
			);
	}

	@Override
	@Transactional
	public CategoriaResponseDto actualizar(Integer id, CategoriaRequestDto request) {
		Categoria categoria = categoriaRepository.findById(id)
				.orElseThrow(() -> new ModeloNotFoundException("Categoria no encontrada"));
		
		categoria.setNombre(request.getNombre());
		
		Categoria actualizada = categoriaRepository.save(categoria);
		
		return new CategoriaResponseDto(
				actualizada.getIdCategoria(),
				actualizada.getNombre()
			);
	}

	@Override
	@Transactional
	public void eliminar(Integer id) {
		Categoria categoria = categoriaRepository.findById(id)
				.orElseThrow(() -> new ModeloNotFoundException("Categoria no encontrada"));
		
		categoria.setActivo(false);
		categoriaRepository.save(categoria);
		
	}
}