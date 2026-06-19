package com.example.apiyazumy.repository;

import com.example.apiyazumy.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    List<Pedido> findByUsuarioIdUsuario(Integer idUsuario);

}