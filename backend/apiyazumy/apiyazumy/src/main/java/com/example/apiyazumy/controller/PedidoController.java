package com.example.apiyazumy.controller;

import com.example.apiyazumy.business.PedidoBusiness;
import com.example.apiyazumy.dto.response.ApiResponse;
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
}
