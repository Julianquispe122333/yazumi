package com.example.apiyazumy.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "productos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Integer idProducto;

    @Column(name = "codigo_producto")
    private String codigoProducto;

    private String nombre;

    private String descripcion;

    private String presentacion;

    private BigDecimal precio;

    private Integer stock;

    private String imagen;

    private Boolean activo;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
}