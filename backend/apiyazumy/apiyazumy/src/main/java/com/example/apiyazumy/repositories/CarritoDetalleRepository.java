package com.example.apiyazumy.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.apiyazumy.entities.Carrito;
import com.example.apiyazumy.entities.CarritoDetalle;
import com.example.apiyazumy.entities.Producto;

@Repository
public interface CarritoDetalleRepository extends JpaRepository<CarritoDetalle, Integer> {

    List<CarritoDetalle> findByCarrito(Carrito carrito);

    List<CarritoDetalle> findByProducto(Producto producto);

}