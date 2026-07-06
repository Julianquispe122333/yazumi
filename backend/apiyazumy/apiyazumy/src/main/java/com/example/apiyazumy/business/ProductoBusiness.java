package com.example.apiyazumy.business;

import com.example.apiyazumy.dto.response.CategoriaResponseDTO;
import com.example.apiyazumy.dto.response.ProductoResponseDTO;

import java.util.List;

public interface ProductoBusiness {
    List<ProductoResponseDTO> listarProductos();
    List<ProductoResponseDTO> buscarPorNombre(String nombre);
    ProductoResponseDTO obtenerPorId(Integer idProducto);
    List<CategoriaResponseDTO> listarCategorias();
}

