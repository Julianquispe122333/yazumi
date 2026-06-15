package com.example.apiyazumy.repositories;

import com.example.apiyazumy.entities.EstadoPedido;
import com.example.apiyazumy.entities.HistorialEstados;
import com.example.apiyazumy.entities.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistorialEstadosRepository
        extends JpaRepository<HistorialEstados, Integer> {

    List<HistorialEstados> findByPedido(Pedido pedido);

    List<HistorialEstados> findByEstado(EstadoPedido estado);

}