package com.example.apiyazumy.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarritoDetalleResponseDTO {

    private Integer idDetalle;
    private Integer idProducto;
    private String nombreProducto;
    private String imagen;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;  // cantidad * precioUnitario
}
