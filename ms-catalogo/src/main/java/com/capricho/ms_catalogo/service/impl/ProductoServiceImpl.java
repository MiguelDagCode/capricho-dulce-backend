package com.capricho.ms_catalogo.service.impl;

import com.capricho.ms_catalogo.dto.request.ProductoRequestDto;
import com.capricho.ms_catalogo.dto.response.ProductoResponseDto;
import com.capricho.ms_catalogo.model.Categoria;
import com.capricho.ms_catalogo.model.Producto;
import com.capricho.ms_catalogo.repository.CategoriaRepository;
import com.capricho.ms_catalogo.repository.ProductoRepository;
import com.capricho.ms_catalogo.service.ProductoService;
import com.capricho.ms_catalogo.util.ModeloNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDto> listarActivos() {

        return productoRepository.findByEstadoTrue()
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoResponseDto obtenerPorId(Long id) {

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ModeloNotFoundException("Producto no encontrado"));

        return convertToDto(producto);
    }

    @Override
    @Transactional
    public ProductoResponseDto registrar(ProductoRequestDto request) {

        Categoria categoria = categoriaRepository.findById(request.getIdCategoria())
                .orElseThrow(() -> new ModeloNotFoundException("Categoría no encontrada"));

        Producto producto = new Producto();
        producto.setNombre(request.getNombre());
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecio(request.getPrecio());
        producto.setImgUrl(request.getImgUrl());
        producto.setCategoria(categoria);
        producto.setEstado(true);

        return convertToDto(productoRepository.save(producto));
    }

    @Override
    @Transactional
    public ProductoResponseDto actualizar(Long id, ProductoRequestDto request) {

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ModeloNotFoundException("Producto no encontrado"));

        Categoria categoria = categoriaRepository.findById(request.getIdCategoria())
                .orElseThrow(() -> new ModeloNotFoundException("Categoría no encontrada"));

        producto.setNombre(request.getNombre());
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecio(request.getPrecio());
        producto.setImgUrl(request.getImgUrl());
        producto.setCategoria(categoria);

        return convertToDto(productoRepository.save(producto));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ModeloNotFoundException("Producto no encontrado"));

        producto.setEstado(false);
        productoRepository.save(producto);
    }

    private ProductoResponseDto convertToDto(Producto producto) {

        return ProductoResponseDto.builder()
                .idProducto(producto.getIdProducto())
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .precio(producto.getPrecio())
                .imgUrl(producto.getImgUrl())
                .categoria(producto.getCategoria().getNombre())
                .build();
    }
}