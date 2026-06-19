package com.example.apiyazumy.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.apiyazumy.entities.Carrito;
import com.example.apiyazumy.entities.Usuario;
import com.example.apiyazumy.repositories.CarritoRepository;

@Service
public class CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private UsuarioService usuarioService;

    public List<Carrito> listarTodos() {
        return carritoRepository.findAll();
    }

    public Carrito obtenerPorId(Integer id) {
        return carritoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));
    }

    public Carrito obtenerPorIdUsuario(Integer idUsuario) {
        Usuario usuario = usuarioService.obtenerPorIdOrThrow(idUsuario);
        return carritoRepository.findByUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("El usuario no tiene carrito"));
    }

    @Transactional
    public Carrito crearCarrito(Integer idUsuario) {
        Usuario usuario = usuarioService.obtenerPorIdOrThrow(idUsuario);
        // Si ya tiene carrito, lo devolvemos
        return carritoRepository.findByUsuario(usuario)
                .orElseGet(() -> {
                    Carrito nuevo = new Carrito(usuario);
                    nuevo.setFechaCreacion(LocalDateTime.now());
                    return carritoRepository.save(nuevo);
                });
    }

    @Transactional
    public void eliminarCarrito(Integer id) {
        carritoRepository.deleteById(id);
    }
}