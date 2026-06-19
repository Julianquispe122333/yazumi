package com.example.apiyazumy.repository;

import com.example.apiyazumy.entity.HistorialEstado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistorialEstadoRepository extends JpaRepository<HistorialEstado, Integer> {

    List<HistorialEstado> findByPedidoIdPedidoOrderByFechaAsc(Integer idPedido);

}