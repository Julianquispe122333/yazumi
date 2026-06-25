package com.example.apiyazumy.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistroResponseDTO {
    private Integer idUsuario;
    private String nombres;
    private String telefono;
    private String direccion;
    private String nombreNegocio;
    private String fechaRegistro;
}
