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
public class CarritoResponseDTO {

    private Integer idCarrito;
    private Integer idUsuario;
    private LocalDateTime fechaCreacion;
    private List<CarritoDetalleResponseDTO> items;
    private Integer totalItems;       // suma de todas las cantidades
    private BigDecimal totalGeneral;  // suma de todos los subtotales
}
