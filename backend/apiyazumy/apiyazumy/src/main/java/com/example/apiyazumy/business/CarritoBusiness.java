package com.example.apiyazumy.business;

import com.example.apiyazumy.dto.request.ActualizarCantidadRequestDTO;
import com.example.apiyazumy.dto.request.AgregarAlCarritoRequestDTO;
import com.example.apiyazumy.dto.request.ComprarRequestDTO;
import com.example.apiyazumy.dto.response.CarritoResponseDTO;
import com.example.apiyazumy.dto.response.CompraResponseDTO;

public interface CarritoBusiness {

    /**
     * Agrega un producto al carrito del usuario.
     * - Si el carrito no existe, lo crea automáticamente.
     * - Si el producto ya está en el carrito, incrementa la cantidad.
     * - Valida stock antes de agregar.
     */
    CarritoResponseDTO agregarProducto(AgregarAlCarritoRequestDTO request);

    /**
     * Retorna el carrito completo del usuario con todos sus ítems,
     * subtotales por ítem y total general.
     */
    CarritoResponseDTO obtenerCarrito(Integer idUsuario);

    /**
     * Actualiza la cantidad de un producto en el carrito.
     * - Si cantidad = 0, elimina el producto del carrito.
     * - Valida stock para la nueva cantidad.
     */
    CarritoResponseDTO actualizarCantidad(ActualizarCantidadRequestDTO request);

    /**
     * Elimina un producto específico del carrito del usuario.
     */
    CarritoResponseDTO eliminarProducto(Integer idUsuario, Integer idProducto);

    /**
     * Elimina todos los productos del carrito, dejándolo vacío.
     * El carrito en sí (registro en tabla carrito) se conserva.
     */
    CarritoResponseDTO vaciarCarrito(Integer idUsuario);

    /**
     * Convierte el carrito en un pedido persistido en base de datos.
     * - Valida que el carrito no esté vacío.
     * - Crea Pedido + DetallePedido en una sola transacción.
     * - Vacía el carrito al finalizar.
     * - El estado inicial del pedido es CONFIRMADO (id_estado=1).
     */
    CompraResponseDTO realizarCompra(Integer idUsuario, ComprarRequestDTO request);
}
