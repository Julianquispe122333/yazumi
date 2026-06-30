package com.example.apiyazumy.business.impl;

import com.example.apiyazumy.business.PedidoBusiness;
import com.example.apiyazumy.dto.response.PedidoResponseDTO;
import com.example.apiyazumy.entity.DetallePedido;
import com.example.apiyazumy.entity.Pedido;
import com.example.apiyazumy.entity.Usuario;
import com.example.apiyazumy.exception.UsuarioNoEncontradoException;
import com.example.apiyazumy.repository.DetallePedidoRepository;
import com.example.apiyazumy.repository.PedidoRepository;
import com.example.apiyazumy.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoBusinessImpl implements PedidoBusiness {

    private final PedidoRepository pedidoRepository;
    private final DetallePedidoRepository detallePedidoRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> obtenerHistorialPedidos(Integer idUsuario) {
        // 1. Validar que el usuario exista
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new UsuarioNoEncontradoException("USUARIO_NO_ENCONTRADO"));

        // 2. Obtener los pedidos ordenados de forma descendente por fecha
        List<Pedido> pedidos = pedidoRepository.findByUsuarioIdUsuarioOrderByFechaPedidoDesc(idUsuario);

        // 3. Mapear pedidos y sus detalles a DTOs de respuesta
        return pedidos.stream().map(pedido -> {
            List<DetallePedido> detalles = detallePedidoRepository.findByPedidoIdPedido(pedido.getIdPedido());
            
            List<PedidoResponseDTO.DetallePedidoResponseDTO> detallesDTO = detalles.stream().map(dp -> 
                PedidoResponseDTO.DetallePedidoResponseDTO.builder()
                        .idProducto(dp.getProducto().getIdProducto())
                        .nombreProducto(dp.getProducto().getNombre())
                        .cantidad(dp.getCantidad())
                        .precioUnitario(dp.getPrecioUnitario())
                        .subtotal(dp.getSubtotal())
                        .build()
            ).toList();

            return PedidoResponseDTO.builder()
                    .idPedido(pedido.getIdPedido())
                    .idUsuario(usuario.getIdUsuario())
                    .estado(pedido.getEstadoPedido() != null ? pedido.getEstadoPedido().getNombre() : null)
                    .fechaPedido(pedido.getFechaPedido())
                    .direccionEntrega(pedido.getDireccionEntrega())
                    .observaciones(pedido.getObservaciones())
                    .detalle(detallesDTO)
                    .total(pedido.getTotal())
                    .build();
        }).toList();
    }
}
