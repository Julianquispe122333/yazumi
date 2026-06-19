package com.example.apiyazumy.controllers;

import com.example.apiyazumy.dtos.LoginRequest;
import com.example.apiyazumy.dtos.RegistroRequest;
import com.example.apiyazumy.entities.Usuario;
import com.example.apiyazumy.services.RecuperacionPasswordService;
import com.example.apiyazumy.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RecuperacionPasswordService recuperacionPasswordService;

    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@RequestBody RegistroRequest request) {
        try {
            Usuario usuario = new Usuario();
            usuario.setNombres(request.getNombres());
            usuario.setTelefono(request.getTelefono());
            usuario.setEmail(request.getEmail());
            usuario.setDireccion(request.getDireccion());
            usuario.setNombreNegocio(request.getNombreNegocio());
            usuario.setPasswordHash(request.getPassword());

            Usuario nuevo = usuarioService.guardarUsuario(usuario);
            nuevo.setPasswordHash(null);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<Usuario> usuarioOpt = usuarioService.login(request.getEmail(), request.getPassword());
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setPasswordHash(null);
            return ResponseEntity.ok(usuario);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Credenciales inválidas"));
    }

    @PostMapping("/recuperar/{email}")
    public ResponseEntity<?> solicitarRecuperacion(@PathVariable String email) {
        try {
            Usuario usuario = usuarioService.buscarPorEmail(email)
                    .orElseThrow(() -> new RuntimeException("Email no registrado"));
            recuperacionPasswordService.generarToken(usuario.getIdUsuario());
            return ResponseEntity.ok(Map.of("mensaje", "Token enviado al correo"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/cambiar-password")
    public ResponseEntity<?> cambiarPassword(@RequestParam String token, @RequestParam String nuevaPassword) {
        try {
            recuperacionPasswordService.cambiarPasswordConToken(token, nuevaPassword);
            return ResponseEntity.ok(Map.of("mensaje", "Contraseña actualizada"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}