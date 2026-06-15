package com.example.apiyazumy.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.apiyazumy.entities.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    Optional<Producto> findByCodigoProducto(String codigoProducto);

    List<Producto> findByActivo(Boolean activo);

    List<Producto> findByNombreContainingIgnoreCase(String nombre);

}