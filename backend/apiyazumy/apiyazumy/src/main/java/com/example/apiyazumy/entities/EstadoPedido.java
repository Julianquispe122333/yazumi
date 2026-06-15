package com.example.apiyazumy.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "estados_pedido")
public class EstadoPedido {

    @Id
    @Column(name = "id_estado")
    private Integer idEstado;

    @Column(nullable = false, length = 50)
    private String nombre;

    public EstadoPedido() {
    }

    public EstadoPedido(Integer idEstado, String nombre) {
        this.idEstado = idEstado;
        this.nombre = nombre;
    }

    public Integer getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(Integer idEstado) {
        this.idEstado = idEstado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
