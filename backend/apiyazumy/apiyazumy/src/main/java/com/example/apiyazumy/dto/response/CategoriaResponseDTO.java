package com.example.apiyazumy.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaResponseDTO {
    private String nombre;           // nombre de la marca/categoría
    private String imagen;           // URL imagen (null por ahora, el front usa placeholder)
    private Integer cantidadProductos;
}
