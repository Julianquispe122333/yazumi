package com.example.apiyazumy.business.impl;

import com.example.apiyazumy.business.AuthBusiness;
import com.example.apiyazumy.dto.request.LoginRequestDTO;
import com.example.apiyazumy.dto.response.LoginResponseDTO;
import com.example.apiyazumy.entity.Usuario;
import com.example.apiyazumy.exception.CodigoInvalidoException;
import com.example.apiyazumy.exception.UsuarioNoEncontradoException;
import com.example.apiyazumy.repository.ConfiguracionRepository;
import com.example.apiyazumy.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthBusinessImpl implements AuthBusiness {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginResponseDTO login(LoginRequestDTO request) {
        // 1. Buscar usuario por telefono
        Usuario usuario = usuarioRepository.findByTelefono(request.getTelefono())
                .orElseThrow(() -> new UsuarioNoEncontradoException("CLIENTE_NO_REGISTRADO"));

        // 2. Validar password
        if (!passwordEncoder.matches(request.getPassword(), usuario.getPasswordHash())) {
            throw new com.example.apiyazumy.exception.CredencialesInvalidasException("CREDenciales_INVALIDAS");
        }

        // 3. Devolver acceso permitido
        return LoginResponseDTO.builder()
                .idUsuario(usuario.getIdUsuario())
                .nombres(usuario.getNombres())
                .telefono(usuario.getTelefono())
                .direccion(usuario.getDireccion())
                .nombreNegocio(usuario.getNombreNegocio())
                .build();
    }
}
