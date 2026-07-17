package com.example.apiyazumy.business.impl;

import com.example.apiyazumy.business.ProductoBusiness;
import com.example.apiyazumy.dto.response.CategoriaResponseDTO;
import com.example.apiyazumy.dto.response.ProductoResponseDTO;
import com.example.apiyazumy.entity.Producto;
import com.example.apiyazumy.exception.ProductoNoEncontradoException;
import com.example.apiyazumy.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductoBusinessImpl implements ProductoBusiness {

    private final ProductoRepository productoRepository;

    // ─── MAPPER PRIVADO ────────────────────────────────────────────────────────

    private ProductoResponseDTO toResponse(Producto p) {
        return ProductoResponseDTO.builder()
                .idProducto(p.getIdProducto())
                .codigoProducto(p.getCodigoProducto())
                .marca(p.getMarca())
                .nombre(p.getNombre())
                .descripcion(p.getDescripcion())
                .presentacion(p.getPresentacion())
                .precio(p.getPrecio())
                .stock(p.getStock())
                .stockEstado(p.getStock() != null && p.getStock() > 0 ? "DISPONIBLE" : "SIN STOCK")
                .imagen(p.getImagen())
                .activo(p.getActivo())
                .unidadesPorPaquete(p.getUnidadesPorPaquete() != null && p.getUnidadesPorPaquete() > 0 ? p.getUnidadesPorPaquete() : 12)
                .precioSugerido(p.getPrecioSugerido() != null && p.getPrecioSugerido().compareTo(java.math.BigDecimal.ZERO) > 0 ? p.getPrecioSugerido() : java.math.BigDecimal.valueOf(1.50))
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

    // ─── LISTAR CATEGORIAS (agrupadas por marca) ───────────────────────────────

    @Override
    public List<CategoriaResponseDTO> listarCategorias() {
        List<Producto> activos = productoRepository.findByActivoTrueOrderByNombreAsc();
        Map<String, List<Producto>> porMarca = activos.stream()
                .filter(p -> p.getMarca() != null && !p.getMarca().isBlank())
                .collect(Collectors.groupingBy(Producto::getMarca));

        return porMarca.entrySet().stream()
                .map(e -> CategoriaResponseDTO.builder()
                        .nombre(e.getKey())
                        .imagen(e.getValue().isEmpty() ? null : e.getValue().get(0).getImagen())
                        .cantidadProductos(e.getValue().size())
                        .build())
                .sorted((a, b) -> a.getNombre().compareToIgnoreCase(b.getNombre()))
                .collect(Collectors.toList());
    }
}
