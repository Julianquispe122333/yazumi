package com.example.apiyazumy.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.apiyazumy.entities.Notificacion;
import com.example.apiyazumy.entities.Usuario;

@Repository
public interface NotificacionRepository
        extends JpaRepository<Notificacion, Integer> {

    List<Notificacion> findByUsuario(Usuario usuario);

    List<Notificacion> findByUsuarioAndLeido(Usuario usuario, Boolean leido);

}