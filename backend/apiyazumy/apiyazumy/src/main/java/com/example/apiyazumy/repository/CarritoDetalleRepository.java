package com.example.apiyazumy.repository;

import com.example.apiyazumy.entity.CarritoDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarritoDetalleRepository extends JpaRepository<CarritoDetalle, Integer> {

    // Obtener todos los detalles de un carrito
    List<CarritoDetalle> findByCarritoIdCarrito(Integer idCarrito);

    // Buscar un producto específico dentro del carrito (para evitar duplicados)
    Optional<CarritoDetalle> findByCarritoIdCarritoAndProductoIdProducto(Integer idCarrito, Integer idProducto);

    // Eliminar todos los detalles de un carrito (vaciar)
    void deleteByCarritoIdCarrito(Integer idCarrito);

    // Eliminar un producto específico del carrito
    void deleteByCarritoIdCarritoAndProductoIdProducto(Integer idCarrito, Integer idProducto);
}
