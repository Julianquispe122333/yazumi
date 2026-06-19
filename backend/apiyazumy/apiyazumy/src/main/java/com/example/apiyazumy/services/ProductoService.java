package com.example.apiyazumy.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.apiyazumy.entities.Producto;
import com.example.apiyazumy.repositories.ProductoRepository;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    public List<Producto> listarActivos() {
        return productoRepository.findByActivo(true);
    }

    public Producto obtenerPorId(Integer id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    public Producto obtenerPorCodigo(String codigo) {
        return productoRepository.findByCodigoProducto(codigo)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    public List<Producto> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    @Transactional
    public Producto crearProducto(Producto producto) {
        if (productoRepository.findByCodigoProducto(producto.getCodigoProducto()).isPresent()) {
            throw new RuntimeException("Código ya existe");
        }
        producto.setActivo(true);
        producto.setFechaActualizacion(LocalDateTime.now());
        return productoRepository.save(producto);
    }

    @Transactional
    public Producto actualizarProducto(Integer id, Producto datos) {
        Producto existente = obtenerPorId(id);
        if (datos.getCodigoProducto() != null) existente.setCodigoProducto(datos.getCodigoProducto());
        if (datos.getNombre() != null) existente.setNombre(datos.getNombre());
        if (datos.getDescripcion() != null) existente.setDescripcion(datos.getDescripcion());
        if (datos.getPresentacion() != null) existente.setPresentacion(datos.getPresentacion());
        if (datos.getPrecio() != null) existente.setPrecio(datos.getPrecio());
        if (datos.getStock() != null) existente.setStock(datos.getStock());
        if (datos.getImagen() != null) existente.setImagen(datos.getImagen());
        existente.setFechaActualizacion(LocalDateTime.now());
        return productoRepository.save(existente);
    }

    @Transactional
    public void eliminarProducto(Integer id) {
        productoRepository.deleteById(id);
    }

    @Transactional
    public void descontarStock(Integer idProducto, Integer cantidad) {
        Producto p = obtenerPorId(idProducto);
        if (p.getStock() < cantidad) throw new RuntimeException("Stock insuficiente");
        p.setStock(p.getStock() - cantidad);
        p.setFechaActualizacion(LocalDateTime.now());
        productoRepository.save(p);
    }

    @Transactional
    public void reponerStock(Integer idProducto, Integer cantidad) {
        Producto p = obtenerPorId(idProducto);
        p.setStock(p.getStock() + cantidad);
        p.setFechaActualizacion(LocalDateTime.now());
        productoRepository.save(p);
    }

    public boolean hayStockSuficiente(Integer idProducto, Integer cantidad) {
        Producto p = obtenerPorId(idProducto);
        return p.getStock() >= cantidad;
    }
}