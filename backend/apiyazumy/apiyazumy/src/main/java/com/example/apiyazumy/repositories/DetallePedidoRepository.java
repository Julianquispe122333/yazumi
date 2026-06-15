package com.example.apiyazumy.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.apiyazumy.entities.DetallePedido;
import com.example.apiyazumy.entities.Pedido;
import com.example.apiyazumy.entities.Producto;

@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Integer> {

    List<DetallePedido> findByPedido(Pedido pedido);

    List<DetallePedido> findByProducto(Producto producto);

}