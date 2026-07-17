package com.example.apiyazumy.controller;

import com.example.apiyazumy.business.PedidoBusiness;
import com.example.apiyazumy.dto.response.ApiResponse;
import com.example.apiyazumy.dto.response.EstadoPedidoResponseDTO;
import com.example.apiyazumy.dto.response.PedidoResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoBusiness pedidoBusiness;

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<ApiResponse<List<PedidoResponseDTO>>> obtenerHistorial(
            @PathVariable Integer idUsuario) {

        List<PedidoResponseDTO> historial = pedidoBusiness.obtenerHistorialPedidos(idUsuario);
        return ResponseEntity.ok(ApiResponse.success("Historial de pedidos obtenido exitosamente", historial));
    }

    @GetMapping("/{idPedido}")
    public ResponseEntity<ApiResponse<PedidoResponseDTO>> obtenerPedido(
            @PathVariable Integer idPedido) {

        PedidoResponseDTO pedido = pedidoBusiness.obtenerPedidoPorId(idPedido);
        return ResponseEntity.ok(ApiResponse.success("Pedido obtenido exitosamente", pedido));
    }

    @GetMapping("/estados")
    public ResponseEntity<ApiResponse<List<EstadoPedidoResponseDTO>>> listarEstados() {
        List<EstadoPedidoResponseDTO> estados = pedidoBusiness.listarEstados();
        return ResponseEntity.ok(ApiResponse.success("Estados obtenidos exitosamente", estados));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PedidoResponseDTO>>> obtenerTodosLosPedidos() {
        List<PedidoResponseDTO> pedidos = pedidoBusiness.obtenerTodosLosPedidos();
        return ResponseEntity.ok(ApiResponse.success("Todos los pedidos obtenidos exitosamente", pedidos));
    }

    @PutMapping("/{idPedido}/estado/{idEstado}")
    public ResponseEntity<ApiResponse<PedidoResponseDTO>> actualizarEstadoPedido(
            @PathVariable Integer idPedido,
            @PathVariable Integer idEstado) {
        PedidoResponseDTO pedido = pedidoBusiness.actualizarEstadoPedido(idPedido, idEstado);
        return ResponseEntity.ok(ApiResponse.success("Estado del pedido actualizado exitosamente", pedido));
    }
}
