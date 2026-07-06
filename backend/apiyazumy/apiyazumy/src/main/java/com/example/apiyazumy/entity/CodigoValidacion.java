package com.example.apiyazumy.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "codigos_validacion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CodigoValidacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_codigo")
    private Integer id;

    @Column(name = "codigo", nullable = false, length = 50)
    private String codigoValidacion;

}