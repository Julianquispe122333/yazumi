package com.example.apiyazumy.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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
    private Integer idCodigo;

    @Column(nullable = false, unique = true, length = 50)
    private String codigo;

    private Boolean usado;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
}