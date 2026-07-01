package com.example.apiyazumy.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstadoPedidoResponseDTO {

    private Integer idEstado;
    private String nombre;
}
