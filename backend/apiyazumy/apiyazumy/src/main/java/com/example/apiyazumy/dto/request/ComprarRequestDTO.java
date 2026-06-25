package com.example.apiyazumy.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComprarRequestDTO {

    // Campos opcionales que el usuario puede enviar al confirmar la compra
    private String direccionEntrega;
    private String observaciones;
}
