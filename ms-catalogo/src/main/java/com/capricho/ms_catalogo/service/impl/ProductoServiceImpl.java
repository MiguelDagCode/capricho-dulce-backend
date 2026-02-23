package com.capricho.ms_catalogo.service.impl;

import com.capricho.ms_catalogo.dto.response.ProductoResponseDto;
import com.capricho.ms_catalogo.model.Producto;
import com.capricho.ms_catalogo.repository.ProductoRepository;
import com.capricho.ms_catalogo.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDto> listarCatalogoActivo() {
        return productoRepository.findByEstadoTrueAndCategoriaActivoTrue()
                .stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    // Método auxiliar privado para mantener el código limpio
    private ProductoResponseDto mapearADto(Producto p) {
        return ProductoResponseDto.builder()
                .id(p.getIdProducto())
                .nombre(p.getNombre())
                .descripcion(p.getDescripcion())
                .precio(p.getPrecio())
                .imagenUrl(p.getImgUrl())
                // Accedemos a la categoría (Hibernate hará la consulta LAZY automáticamente aquí)
                .categoria(p.getCategoria().getNombre())
                .idCategoria(p.getCategoria().getIdCategoria())
                .build();
    }
}