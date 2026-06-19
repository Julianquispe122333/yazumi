package com.example.apiyazumy.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.apiyazumy.entities.Notificacion;
import com.example.apiyazumy.entities.Usuario;
import com.example.apiyazumy.repositories.NotificacionRepository;

@Service
public class NotificacionService {

    @Autowired
    private NotificacionRepository notificacionRepository;

    @Autowired
    private UsuarioService usuarioService;

    public List<Notificacion> listarTodas() {
        return notificacionRepository.findAll();
    }

    public Notificacion obtenerPorId(Integer id) {
        return notificacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada"));
    }

    public List<Notificacion> obtenerPorUsuario(Integer idUsuario) {
        Usuario usuario = usuarioService.obtenerPorIdOrThrow(idUsuario);
        return notificacionRepository.findByUsuario(usuario);
    }

    public List<Notificacion> obtenerNoLeidas(Integer idUsuario) {
        Usuario usuario = usuarioService.obtenerPorIdOrThrow(idUsuario);
        return notificacionRepository.findByUsuarioAndLeido(usuario, false);
    }

    @Transactional
    public Notificacion crearNotificacion(Integer idUsuario, String titulo, String mensaje) {
        Usuario usuario = usuarioService.obtenerPorIdOrThrow(idUsuario);
        Notificacion notificacion = new Notificacion(usuario, titulo, mensaje);
        notificacion.setFecha(LocalDateTime.now());
        notificacion.setLeido(false);
        return notificacionRepository.save(notificacion);
    }

    @Transactional
    public Notificacion marcarComoLeida(Integer idNotificacion) {
        Notificacion notificacion = obtenerPorId(idNotificacion);
        notificacion.setLeido(true);
        return notificacionRepository.save(notificacion);
    }

    @Transactional
    public void marcarTodasComoLeidas(Integer idUsuario) {
        List<Notificacion> noLeidas = obtenerNoLeidas(idUsuario);
        noLeidas.forEach(n -> n.setLeido(true));
        notificacionRepository.saveAll(noLeidas);
    }

    @Transactional
    public void eliminarNotificacion(Integer id) {
        notificacionRepository.deleteById(id);
    }
}