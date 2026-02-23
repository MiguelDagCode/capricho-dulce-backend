package com.capricho.ms_catalogo.service.impl;

import com.capricho.ms_catalogo.dto.request.RecetaRequestDto;
import com.capricho.ms_catalogo.dto.response.RecetaResponseDto;
import com.capricho.ms_catalogo.model.*;
import com.capricho.ms_catalogo.repository.*;
import com.capricho.ms_catalogo.service.RecetaService;
import com.capricho.ms_catalogo.util.ModeloNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecetaServiceImpl implements RecetaService {

    private final RecetaRepository recetaRepository;
    private final ProductoRepository productoRepository;
    private final InsumoRepository insumoRepository;

    @Override
    @Transactional
    public RecetaResponseDto registrar(RecetaRequestDto request) {

        Producto producto = productoRepository.findById(request.getIdProducto())
                .orElseThrow(() -> new ModeloNotFoundException("Producto no encontrado"));

        Insumo insumo = insumoRepository.findById(request.getIdInsumo())
                .orElseThrow(() -> new ModeloNotFoundException("Insumo no encontrado"));

        RecetaId recetaId = new RecetaId(
                request.getIdProducto(),
                request.getIdInsumo()
        );

        Receta receta = new Receta();
        receta.setId(recetaId);
        receta.setProducto(producto);
        receta.setInsumo(insumo);
        receta.setCantidadInsumo(request.getCantidadInsumo());
        receta.setActivo(true);

        return convertir(recetaRepository.save(receta));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecetaResponseDto> listarPorProducto(Long idProducto) {

        return recetaRepository.findByProductoIdProductoAndActivoTrue(idProducto)
                .stream()
                .map(this::convertir)
                .toList();
    }

    @Override
    @Transactional
    public void eliminar(Long idProducto, Long idInsumo) {

        RecetaId recetaId = new RecetaId(idProducto, idInsumo);

        Receta receta = recetaRepository.findById(recetaId)
                .orElseThrow(() -> new ModeloNotFoundException("Receta no encontrada"));

        receta.setActivo(false);
        recetaRepository.save(receta);
    }

    private RecetaResponseDto convertir(Receta receta) {

        return RecetaResponseDto.builder()
                .idProducto(receta.getProducto().getIdProducto())
                .idInsumo(receta.getInsumo().getIdInsumo())
                .nombreProducto(receta.getProducto().getNombre())
                .nombreInsumo(receta.getInsumo().getNombre())
                .cantidadInsumo(receta.getCantidadInsumo())
                .build();
    }
}