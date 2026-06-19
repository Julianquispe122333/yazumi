package com.example.apiyazumy.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.apiyazumy.entities.Carrito;
import com.example.apiyazumy.entities.CarritoDetalle;
import com.example.apiyazumy.entities.Producto;
import com.example.apiyazumy.repositories.CarritoDetalleRepository;

@Service
public class CarritoDetalleService {

    @Autowired
    private CarritoDetalleRepository carritoDetalleRepository;

    @Autowired
    private CarritoService carritoService;

    @Autowired
    private ProductoService productoService;

    public List<CarritoDetalle> listarTodos() {
        return carritoDetalleRepository.findAll();
    }

    public CarritoDetalle obtenerPorId(Integer id) {
        return carritoDetalleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Detalle no encontrado"));
    }

    public List<CarritoDetalle> obtenerPorCarrito(Integer idCarrito) {
        Carrito carrito = carritoService.obtenerPorId(idCarrito);
        return carritoDetalleRepository.findByCarrito(carrito);
    }

    @Transactional
    public CarritoDetalle agregarProducto(Integer idCarrito, Integer idProducto, Integer cantidad) {
        Carrito carrito = carritoService.obtenerPorId(idCarrito);
        Producto producto = productoService.obtenerPorId(idProducto);

        // Buscar si el producto ya está en el carrito (filtro en memoria porque no tenemos findByCarritoAndProducto)
        List<CarritoDetalle> detalles = carritoDetalleRepository.findByCarrito(carrito);
        CarritoDetalle existente = detalles.stream()
                .filter(d -> d.getProducto().getIdProducto().equals(idProducto))
                .findFirst()
                .orElse(null);

        if (existente != null) {
            existente.setCantidad(existente.getCantidad() + cantidad);
            return carritoDetalleRepository.save(existente);
        } else {
            CarritoDetalle nuevo = new CarritoDetalle(carrito, producto, cantidad);
            return carritoDetalleRepository.save(nuevo);
        }
    }

    @Transactional
    public void eliminarProducto(Integer idDetalle) {
        carritoDetalleRepository.deleteById(idDetalle);
    }

    @Transactional
    public void vaciarCarrito(Integer idCarrito) {
        Carrito carrito = carritoService.obtenerPorId(idCarrito);
        List<CarritoDetalle> detalles = carritoDetalleRepository.findByCarrito(carrito);
        carritoDetalleRepository.deleteAll(detalles);
    }

    @Transactional
    public CarritoDetalle actualizarCantidad(Integer idDetalle, Integer nuevaCantidad) {
        CarritoDetalle detalle = obtenerPorId(idDetalle);
        if (nuevaCantidad <= 0) {
            eliminarProducto(idDetalle);
            return null;
        }
        detalle.setCantidad(nuevaCantidad);
        return carritoDetalleRepository.save(detalle);
    }
}