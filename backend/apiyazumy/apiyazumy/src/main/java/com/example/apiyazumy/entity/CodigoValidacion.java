package com.example.apiyazumy.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "configuracion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CodigoValidacion {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "codigo_validacion", nullable = false, length = 50)
    private String codigoValidacion;

}