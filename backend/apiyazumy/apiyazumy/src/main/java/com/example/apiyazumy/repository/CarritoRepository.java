package com.example.apiyazumy.repository;

import com.example.apiyazumy.entity.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarritoRepository extends JpaRepository<Carrito, Integer> {

    Optional<Carrito> findByUsuarioIdUsuario(Integer idUsuario);

}