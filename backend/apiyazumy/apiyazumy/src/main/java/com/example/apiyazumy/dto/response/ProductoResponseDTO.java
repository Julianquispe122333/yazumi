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
public class ProductoResponseDTO {
    private Integer idProducto;
    private String codigoProducto;
    private String marca;
    private String nombre;
    private String descripcion;
    private String presentacion;
    private BigDecimal precio;
    private Integer stock;
    private String stockEstado;   // "DISPONIBLE" o "SIN STOCK"
    private String imagen;
    private Boolean activo;
}

