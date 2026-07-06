package com.example.apiyazumy.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompraResponseDTO {

    private Integer idPedido;
    private Integer idUsuario;
    private String estado;              // "CONFIRMADO"
    private LocalDateTime fechaCompra;
    private String direccionEntrega;
    private List<DetallePedidoResponseDTO> detalle;
    private BigDecimal total;

    // ─── DTO interno para el detalle de cada ítem del pedido ─────────────────
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetallePedidoResponseDTO {
        private Integer idProducto;
        private String nombreProducto;
        private Integer cantidad;
        private BigDecimal precioUnitario;
        private BigDecimal subtotal;
    }
}
