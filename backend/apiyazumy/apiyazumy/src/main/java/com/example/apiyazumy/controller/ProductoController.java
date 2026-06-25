package com.example.apiyazumy.controller;

import com.example.apiyazumy.business.ProductoBusiness;
import com.example.apiyazumy.dto.response.ApiResponse;
import com.example.apiyazumy.dto.response.ProductoResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoBusiness productoBusiness;

    // GET /api/productos
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductoResponseDTO>>> listar() {
        return ResponseEntity.ok(ApiResponse.success(
                "Productos obtenidos exitosamente",
                productoBusiness.listarProductos()));
    }

    // GET /api/productos/buscar?nombre=xxx
    @GetMapping("/buscar")
    public ResponseEntity<ApiResponse<List<ProductoResponseDTO>>> buscar(@RequestParam String nombre) {
        return ResponseEntity.ok(ApiResponse.success(
                "Búsqueda completada",
                productoBusiness.buscarPorNombre(nombre)));
    }

    // GET /api/productos/{idProducto}
    @GetMapping("/{idProducto}")
    public ResponseEntity<ApiResponse<ProductoResponseDTO>> obtenerPorId(@PathVariable Integer idProducto) {
        return ResponseEntity.ok(ApiResponse.success(
                "Producto encontrado",
                productoBusiness.obtenerPorId(idProducto)));
    }
}
