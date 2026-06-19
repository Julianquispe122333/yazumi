package com.example.apiyazumy.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.apiyazumy.entities.EstadoPedido;
import com.example.apiyazumy.entities.HistorialEstados;
import com.example.apiyazumy.entities.Pedido;
import com.example.apiyazumy.repositories.HistorialEstadosRepository;

@Service
public class HistorialEstadosService {

    @Autowired
    private HistorialEstadosRepository historialEstadosRepository;

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private EstadoPedidoService estadoPedidoService;

    public List<HistorialEstados> listarTodos() {
        return historialEstadosRepository.findAll();
    }

    public List<HistorialEstados> obtenerPorPedido(Integer idPedido) {
        Pedido pedido = pedidoService.obtenerPorId(idPedido);
        return historialEstadosRepository.findByPedido(pedido);
    }

    @Transactional
    public HistorialEstados registrarCambio(Integer idPedido, Integer idEstado, String comentario) {
        Pedido pedido = pedidoService.obtenerPorId(idPedido);
        EstadoPedido estado = estadoPedidoService.obtenerPorId(idEstado);
        HistorialEstados historial = new HistorialEstados(pedido, estado, comentario);
        historial.setFecha(LocalDateTime.now());
        return historialEstadosRepository.save(historial);
    }
}