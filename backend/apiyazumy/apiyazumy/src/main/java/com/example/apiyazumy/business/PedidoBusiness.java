package com.example.apiyazumy.business;

import com.example.apiyazumy.dto.response.PedidoResponseDTO;
import java.util.List;

public interface PedidoBusiness {
    /**
     * Obtiene el historial de pedidos de un usuario ordenados del más reciente al más antiguo.
     * @param idUsuario Identificador del usuario
     * @return Lista de pedidos con sus respectivos detalles
     */
    List<PedidoResponseDTO> obtenerHistorialPedidos(Integer idUsuario);
}
