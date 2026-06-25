package com.example.apiyazumy.business.impl;

import com.example.apiyazumy.business.ProductoBusiness;
import com.example.apiyazumy.dto.response.ProductoResponseDTO;
import com.example.apiyazumy.entity.Producto;
import com.example.apiyazumy.exception.ProductoNoEncontradoException;
import com.example.apiyazumy.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoBusinessImpl implements ProductoBusiness {

    private final ProductoRepository productoRepository;

    // ─── MAPPER PRIVADO ────────────────────────────────────────────────────────

    private ProductoResponseDTO toResponse(Producto p) {
        return ProductoResponseDTO.builder()
                .idProducto(p.getIdProducto())
                .codigoProducto(p.getCodigoProducto())
                .nombre(p.getNombre())
                .descripcion(p.getDescripcion())
                .presentacion(p.getPresentacion())
                .precio(p.getPrecio())
                .stock(p.getStock())
                .stockEstado(p.getStock() != null && p.getStock() > 0 ? "DISPONIBLE" : "SIN STOCK")
                .imagen(p.getImagen())
                .activo(p.getActivo())
                .build();
    }

    // ─── LISTAR ACTIVOS ───────────────────────────────────────────────────────

    @Override
    public List<ProductoResponseDTO> listarProductos() {
        return productoRepository.findByActivoTrueOrderByNombreAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // ─── BUSCAR POR NOMBRE ────────────────────────────────────────────────────

    @Override
    public List<ProductoResponseDTO> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCaseAndActivoTrue(nombre)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // ─── OBTENER POR ID ───────────────────────────────────────────────────────

    @Override
    public ProductoResponseDTO obtenerPorId(Integer idProducto) {
        Producto producto = productoRepository.findById(idProducto)
                .filter(p -> Boolean.TRUE.equals(p.getActivo()))
                .orElseThrow(() -> new ProductoNoEncontradoException("PRODUCTO_NO_ENCONTRADO"));
        return toResponse(producto);
    }
}
