package com.capricho.ms_catalogo.service.impl;

import com.capricho.ms_catalogo.dto.request.InsumoRequestDto;
import com.capricho.ms_catalogo.dto.response.InsumoResponseDto;
import com.capricho.ms_catalogo.model.Insumo;
import com.capricho.ms_catalogo.repository.InsumoRepository;
import com.capricho.ms_catalogo.service.InsumoService;
import com.capricho.ms_catalogo.util.ModeloNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InsumoServiceImpl implements InsumoService {

    private final InsumoRepository insumoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<InsumoResponseDto> listarActivos() {

        return insumoRepository.findByActivoTrue()
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public InsumoResponseDto obtenerPorId(Long id) {

        Insumo insumo = insumoRepository.findById(id)
                .orElseThrow(() -> new ModeloNotFoundException("Insumo no encontrado"));

        return convertToDto(insumo);
    }

    @Override
    @Transactional
    public InsumoResponseDto registrar(InsumoRequestDto request) {

        Insumo insumo = new Insumo();
        insumo.setNombre(request.getNombre());
        insumo.setUnidadMedida(request.getUnidadMedida());
        insumo.setActivo(true);

        return convertToDto(insumoRepository.save(insumo));
    }

    @Override
    @Transactional
    public InsumoResponseDto actualizar(Long id, InsumoRequestDto request) {

        Insumo insumo = insumoRepository.findById(id)
                .orElseThrow(() -> new ModeloNotFoundException("Insumo no encontrado"));

        insumo.setNombre(request.getNombre());
        insumo.setUnidadMedida(request.getUnidadMedida());

        return convertToDto(insumoRepository.save(insumo));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {

        Insumo insumo = insumoRepository.findById(id)
                .orElseThrow(() -> new ModeloNotFoundException("Insumo no encontrado"));

        insumo.setActivo(false);
        insumoRepository.save(insumo);
    }

    private InsumoResponseDto convertToDto(Insumo insumo) {

        return InsumoResponseDto.builder()
                .idInsumo(insumo.getIdInsumo())
                .nombre(insumo.getNombre())
                .unidadMedida(insumo.getUnidadMedida())
                .build();
    }
}