package com.example.apiyazumy.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "estados_pedido")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstadoPedido {

    @Id
    @Column(name = "id_estado")
    private Integer idEstado;

    private String nombre;
}