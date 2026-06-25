package com.example.apiyazumy.repository;

import com.example.apiyazumy.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByTelefono(String telefono);

    boolean existsByTelefono(String telefono);
}
