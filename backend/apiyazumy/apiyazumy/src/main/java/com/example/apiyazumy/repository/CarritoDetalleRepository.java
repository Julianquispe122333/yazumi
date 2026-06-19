package com.example.apiyazumy.repository;

import com.example.apiyazumy.entity.CarritoDetalle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CarritoDetalleRepository extends JpaRepository<CarritoDetalle, Integer> {

    List<CarritoDetalle> findByCarritoIdCarrito(Integer idCarrito);

    Optional<CarritoDetalle> findByCarritoIdCarritoAndProductoIdProducto(
            Integer idCarrito,
            Integer idProducto
    );

}