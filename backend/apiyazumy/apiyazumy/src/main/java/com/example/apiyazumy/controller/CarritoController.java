package com.example.apiyazumy.controller;

import com.example.apiyazumy.business.CarritoBusiness;
import com.example.apiyazumy.dto.request.ActualizarCantidadRequestDTO;
import com.example.apiyazumy.dto.request.AgregarAlCarritoRequestDTO;
import com.example.apiyazumy.dto.request.ComprarRequestDTO;
import com.example.apiyazumy.dto.response.ApiResponse;
import com.example.apiyazumy.dto.response.CarritoResponseDTO;
import com.example.apiyazumy.dto.response.CompraResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carrito")
@RequiredArgsConstructor
public class CarritoController {

    private final CarritoBusiness carritoBusiness;

    // ─── POST /api/carrito/agregar ────────────────────────────────────────────
    @PostMapping("/agregar")
    public ResponseEntity<ApiResponse<CarritoResponseDTO>> agregarProducto(
            @Valid @RequestBody AgregarAlCarritoRequestDTO request) {

        CarritoResponseDTO carrito = carritoBusiness.agregarProducto(request);
        return ResponseEntity.ok(ApiResponse.success("Producto agregado al carrito", carrito));
    }

    // ─── GET /api/carrito/{idUsuario} ─────────────────────────────────────────
    @GetMapping("/{idUsuario}")
    public ResponseEntity<ApiResponse<CarritoResponseDTO>> obtenerCarrito(
            @PathVariable Integer idUsuario) {

        CarritoResponseDTO carrito = carritoBusiness.obtenerCarrito(idUsuario);
        return ResponseEntity.ok(ApiResponse.success("Carrito obtenido exitosamente", carrito));
    }

    // ─── PUT /api/carrito/actualizar ─────────────────────────────────────────
    @PutMapping("/actualizar")
    public ResponseEntity<ApiResponse<CarritoResponseDTO>> actualizarCantidad(
            @Valid @RequestBody ActualizarCantidadRequestDTO request) {

        CarritoResponseDTO carrito = carritoBusiness.actualizarCantidad(request);
        return ResponseEntity.ok(ApiResponse.success("Cantidad actualizada", carrito));
    }

    // ─── DELETE /api/carrito/{idUsuario}/{idProducto} ─────────────────────────
    @DeleteMapping("/{idUsuario}/{idProducto}")
    public ResponseEntity<ApiResponse<CarritoResponseDTO>> eliminarProducto(
            @PathVariable Integer idUsuario,
            @PathVariable Integer idProducto) {

        CarritoResponseDTO carrito = carritoBusiness.eliminarProducto(idUsuario, idProducto);
        return ResponseEntity.ok(ApiResponse.success("Producto eliminado del carrito", carrito));
    }

    // ─── DELETE /api/carrito/vaciar/{idUsuario} ───────────────────────────────
    @DeleteMapping("/vaciar/{idUsuario}")
    public ResponseEntity<ApiResponse<CarritoResponseDTO>> vaciarCarrito(
            @PathVariable Integer idUsuario) {

        CarritoResponseDTO carrito = carritoBusiness.vaciarCarrito(idUsuario);
        return ResponseEntity.ok(ApiResponse.success("Carrito vaciado exitosamente", carrito));
    }

    // ─── POST /api/carrito/comprar/{idUsuario} ────────────────────────────────
    @PostMapping("/comprar/{idUsuario}")
    public ResponseEntity<ApiResponse<CompraResponseDTO>> realizarCompra(
            @PathVariable Integer idUsuario,
            @RequestBody(required = false) ComprarRequestDTO request) {

        CompraResponseDTO compra = carritoBusiness.realizarCompra(idUsuario, request);
        return ResponseEntity.ok(ApiResponse.success("Compra realizada exitosamente", compra));
    }
}
