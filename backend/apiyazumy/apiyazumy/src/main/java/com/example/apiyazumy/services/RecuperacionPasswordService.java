package com.example.apiyazumy.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.apiyazumy.entities.RecuperacionPassword;
import com.example.apiyazumy.entities.Usuario;
import com.example.apiyazumy.repositories.RecuperacionPasswordRepository;

@Service
public class RecuperacionPasswordService {

    @Autowired
    private RecuperacionPasswordRepository recuperacionPasswordRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private NotificacionService notificacionService;

    @Transactional
    public RecuperacionPassword generarToken(Integer idUsuario) {
        Usuario usuario = usuarioService.obtenerPorIdOrThrow(idUsuario);
        // Invalidar tokens anteriores no usados
        List<RecuperacionPassword> anteriores = recuperacionPasswordRepository.findByUsuarioAndUsadoFalse(usuario);
        anteriores.forEach(t -> t.setUsado(true));
        recuperacionPasswordRepository.saveAll(anteriores);

        RecuperacionPassword recuperacion = new RecuperacionPassword();
        recuperacion.setUsuario(usuario);
        recuperacion.setToken(UUID.randomUUID().toString());
        recuperacion.setFechaExpiracion(LocalDateTime.now().plusHours(24));
        recuperacion.setUsado(false);
        RecuperacionPassword saved = recuperacionPasswordRepository.save(recuperacion);

        notificacionService.crearNotificacion(idUsuario, "Recuperación de contraseña",
                "Se ha generado un token de recuperación. Expira en 24 horas.");
        return saved;
    }

    public RecuperacionPassword validarToken(String token) {
        RecuperacionPassword recuperacion = recuperacionPasswordRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token inválido"));
        if (recuperacion.getUsado()) {
            throw new RuntimeException("Token ya usado");
        }
        if (recuperacion.getFechaExpiracion().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expirado");
        }
        return recuperacion;
    }

    @Transactional
    public void cambiarPasswordConToken(String token, String nuevaPassword) {
        RecuperacionPassword recuperacion = validarToken(token);
        Usuario usuario = recuperacion.getUsuario();
        usuarioService.cambiarPassword(usuario.getIdUsuario(), nuevaPassword);
        recuperacion.setUsado(true);
        recuperacionPasswordRepository.save(recuperacion);
        notificacionService.crearNotificacion(usuario.getIdUsuario(), "Contraseña actualizada",
                "Tu contraseña ha sido cambiada exitosamente.");
    }
}