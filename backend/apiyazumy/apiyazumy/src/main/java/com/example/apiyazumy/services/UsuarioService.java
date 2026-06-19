package com.example.apiyazumy.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.apiyazumy.entities.Usuario;
import com.example.apiyazumy.repositories.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public Usuario guardarUsuario(Usuario usuario) {
        if (usuario.getEmail() != null && usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }
        if (usuarioRepository.findByTelefono(usuario.getTelefono()).isPresent()) {
            throw new RuntimeException("El teléfono ya está registrado");
        }
        usuario.setPasswordHash(passwordEncoder.encode(usuario.getPasswordHash()));
        usuario.setEstado(true);
        usuario.setFechaRegistro(LocalDateTime.now());
        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> obtenerPorId(Integer id) {
        return usuarioRepository.findById(id);
    }

    public Usuario obtenerPorIdOrThrow(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Optional<Usuario> buscarPorTelefono(String telefono) {
        return usuarioRepository.findByTelefono(telefono);
    }

    @Transactional
    public void eliminarUsuario(Integer id) {
        Usuario usuario = obtenerPorIdOrThrow(id);
        usuario.setEstado(false);
        usuarioRepository.save(usuario);
    }

    public Optional<Usuario> login(String email, String passwordPlain) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        if (usuarioOpt.isPresent() && passwordEncoder.matches(passwordPlain, usuarioOpt.get().getPasswordHash())) {
            return usuarioOpt;
        }
        return Optional.empty();
    }

    @Transactional
    public Usuario actualizarUsuario(Integer id, Usuario datosActualizados) {
        Usuario existente = obtenerPorIdOrThrow(id);
        if (datosActualizados.getNombres() != null) existente.setNombres(datosActualizados.getNombres());
        if (datosActualizados.getTelefono() != null) {
            Optional<Usuario> telefonoExistente = usuarioRepository.findByTelefono(datosActualizados.getTelefono());
            if (telefonoExistente.isPresent() && !telefonoExistente.get().getIdUsuario().equals(id)) {
                throw new RuntimeException("El teléfono ya está registrado por otro usuario");
            }
            existente.setTelefono(datosActualizados.getTelefono());
        }
        if (datosActualizados.getEmail() != null) {
            Optional<Usuario> emailExistente = usuarioRepository.findByEmail(datosActualizados.getEmail());
            if (emailExistente.isPresent() && !emailExistente.get().getIdUsuario().equals(id)) {
                throw new RuntimeException("El email ya está registrado por otro usuario");
            }
            existente.setEmail(datosActualizados.getEmail());
        }
        if (datosActualizados.getDireccion() != null) existente.setDireccion(datosActualizados.getDireccion());
        if (datosActualizados.getNombreNegocio() != null) existente.setNombreNegocio(datosActualizados.getNombreNegocio());
        if (datosActualizados.getPasswordHash() != null && !datosActualizados.getPasswordHash().isEmpty()) {
            existente.setPasswordHash(passwordEncoder.encode(datosActualizados.getPasswordHash()));
        }
        return usuarioRepository.save(existente);
    }

    @Transactional
    public void cambiarPassword(Integer idUsuario, String nuevaPassword) {
        Usuario usuario = obtenerPorIdOrThrow(idUsuario);
        usuario.setPasswordHash(passwordEncoder.encode(nuevaPassword));
        usuarioRepository.save(usuario);
    }
}