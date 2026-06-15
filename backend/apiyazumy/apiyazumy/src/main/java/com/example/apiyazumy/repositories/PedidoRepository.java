package com.example.apiyazumy.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.apiyazumy.entities.EstadoPedido;
import com.example.apiyazumy.entities.Pedido;
import com.example.apiyazumy.entities.Usuario;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    List<Pedido> findByUsuario(Usuario usuario);

    List<Pedido> findByEstado(EstadoPedido estado);

    List<Pedido> findByUsuarioAndEstado(Usuario usuario, EstadoPedido estado);

}