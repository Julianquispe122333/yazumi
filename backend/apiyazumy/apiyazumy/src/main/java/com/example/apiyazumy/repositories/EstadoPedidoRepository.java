package com.example.apiyazumy.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.apiyazumy.entities.EstadoPedido;

@Repository
public interface EstadoPedidoRepository extends JpaRepository<EstadoPedido, Integer> {

    Optional<EstadoPedido> findByNombre(String nombre);

}