package com.example.apiyazumy.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.apiyazumy.entities.DetallePedido;
import com.example.apiyazumy.entities.Pedido;
import com.example.apiyazumy.repositories.DetallePedidoRepository;

@Service
public class DetallePedidoService {

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    @Autowired
    private PedidoService pedidoService;

    public List<DetallePedido> listarTodos() {
        return detallePedidoRepository.findAll();
    }

    public DetallePedido obtenerPorId(Integer id) {
        return detallePedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Detalle no encontrado"));
    }

    public List<DetallePedido> obtenerPorPedido(Integer idPedido) {
        Pedido pedido = pedidoService.obtenerPorId(idPedido);
        return detallePedidoRepository.findByPedido(pedido);
    }
}