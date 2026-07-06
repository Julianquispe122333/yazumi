package com.example.apiyazumy.business.impl;

import com.example.apiyazumy.business.UsuarioBusiness;
import com.example.apiyazumy.dto.request.RegistroRequestDTO;
import com.example.apiyazumy.dto.response.RegistroResponseDTO;
import com.example.apiyazumy.entity.Usuario;
import com.example.apiyazumy.exception.CodigoInvalidoException;
import com.example.apiyazumy.exception.TelefonoExistenteException;
import com.example.apiyazumy.repository.ConfiguracionRepository;
import com.example.apiyazumy.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UsuarioBusinessImpl implements UsuarioBusiness {

    private final UsuarioRepository usuarioRepository;
    private final ConfiguracionRepository configuracionRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public RegistroResponseDTO registrar(RegistroRequestDTO request) {
        // 1. Validar código en configuracion.
        com.example.apiyazumy.entity.CodigoValidacion cod = configuracionRepository.findByCodigoValidacion(request.getCodigoValidacion())
                .orElseThrow(() -> new CodigoInvalidoException("CODIGO_INVALIDO"));

        // 2. Verificar que teléfono no exista.
        if (usuarioRepository.existsByTelefono(request.getTelefono())) {
            throw new TelefonoExistenteException("EL TELÉFONO YA ESTÁ REGISTRADO");
        }

        // 3. Crear usuario con password encriptado.
        Usuario nuevoUsuario = Usuario.builder()
                .nombres(request.getNombres())
                .telefono(request.getTelefono())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .direccion(request.getDireccion())
                .nombreNegocio(request.getNombreNegocio())
                .estado(true)
                .fechaRegistro(LocalDateTime.now())
                .codigoValidacion(cod)
                .build();

        nuevoUsuario = usuarioRepository.save(nuevoUsuario);

        // 4. Retornar datos del usuario creado sin el password.
        return RegistroResponseDTO.builder()
                .idUsuario(nuevoUsuario.getIdUsuario())
                .nombres(nuevoUsuario.getNombres())
                .telefono(nuevoUsuario.getTelefono())
                .direccion(nuevoUsuario.getDireccion())
                .nombreNegocio(nuevoUsuario.getNombreNegocio())
                .fechaRegistro(
                        nuevoUsuario.getFechaRegistro() != null ? nuevoUsuario.getFechaRegistro().toString() : null)
                .build();
    }
}
