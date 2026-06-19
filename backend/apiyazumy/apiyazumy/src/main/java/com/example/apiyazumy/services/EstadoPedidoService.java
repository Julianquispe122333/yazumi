package com.example.apiyazumy.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.apiyazumy.entities.EstadoPedido;
import com.example.apiyazumy.repositories.EstadoPedidoRepository;

@Service
public class EstadoPedidoService {

    @Autowired
    private EstadoPedidoRepository estadoPedidoRepository;

    public List<EstadoPedido> listarTodos() {
        return estadoPedidoRepository.findAll();
    }

    public EstadoPedido obtenerPorId(Integer id) {
        return estadoPedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estado no encontrado"));
    }

    public EstadoPedido obtenerPorNombre(String nombre) {
        return estadoPedidoRepository.findByNombre(nombre)
                .orElseThrow(() -> new RuntimeException("Estado no encontrado: " + nombre));
    }

    @Transactional
    public EstadoPedido crearEstado(EstadoPedido estado) {
        // Si ya existe, lanzar excepción
        if (estadoPedidoRepository.findByNombre(estado.getNombre()).isPresent()) {
            throw new RuntimeException("El estado ya existe");
        }
        return estadoPedidoRepository.save(estado);
    }

    @Transactional
    public EstadoPedido actualizarEstado(Integer id, EstadoPedido datos) {
        EstadoPedido existente = obtenerPorId(id);
        if (datos.getNombre() != null) existente.setNombre(datos.getNombre());
        return estadoPedidoRepository.save(existente);
    }

    @Transactional
    public void eliminarEstado(Integer id) {
        estadoPedidoRepository.deleteById(id);
    }
}