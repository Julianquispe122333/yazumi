package com.example.apiyazumy.business;

import com.example.apiyazumy.dto.response.EstadoPedidoResponseDTO;
import com.example.apiyazumy.dto.response.PedidoResponseDTO;

import java.util.List;

public interface PedidoBusiness {
    /**
     * Obtiene el historial de pedidos de un usuario ordenados del más reciente al más antiguo.
     * @param idUsuario Identificador del usuario
     * @return Lista de pedidos con sus respectivos detalles
     */
    List<PedidoResponseDTO> obtenerHistorialPedidos(Integer idUsuario);

    /**
     * Obtiene un pedido específico por su ID, incluyendo su estado actual.
     * @param idPedido Identificador del pedido
     * @return DTO con la información del pedido y su estado actual
     */
    PedidoResponseDTO obtenerPedidoPorId(Integer idPedido);

    /**
     * Retorna todos los estados de pedido disponibles.
     * @return Lista de estados (id + nombre)
     */
    List<EstadoPedidoResponseDTO> listarEstados();
}
