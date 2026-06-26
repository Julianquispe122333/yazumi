package com.example.apiyazumy.repository;

import com.example.apiyazumy.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    List<Pedido> findByUsuarioIdUsuarioOrderByFechaPedidoDesc(Integer idUsuario);
}
