package com.example.apiyazumy.repository;

import com.example.apiyazumy.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByTelefono(String telefono);

    boolean existsByTelefono(String telefono);

}