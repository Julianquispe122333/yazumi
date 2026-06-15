package com.example.apiyazumy.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.apiyazumy.entities.Carrito;
import com.example.apiyazumy.entities.Usuario;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Integer> {

    Optional<Carrito> findByUsuario(Usuario usuario);

}