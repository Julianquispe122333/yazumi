package com.example.apiyazumy.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    private String nombres;

    @Column(unique = true)
    private String telefono;

    private String direccion;

    @Column(name = "nombre_negocio")
    private String nombreNegocio;

    @Column(name = "password_hash")
    private String passwordHash;

    private Boolean estado;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    @ManyToOne
    @JoinColumn(name = "id_codigo")
    private CodigoValidacion codigoValidacion;
}