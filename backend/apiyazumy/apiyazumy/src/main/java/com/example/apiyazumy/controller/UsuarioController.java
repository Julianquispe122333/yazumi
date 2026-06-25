package com.example.apiyazumy.controller;

import com.example.apiyazumy.business.UsuarioBusiness;
import com.example.apiyazumy.dto.request.RegistroRequestDTO;
import com.example.apiyazumy.dto.response.ApiResponse;
import com.example.apiyazumy.dto.response.RegistroResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioBusiness usuarioBusiness;

    @PostMapping("/registrar")
    public ResponseEntity<ApiResponse<RegistroResponseDTO>> registrar(@Valid @RequestBody RegistroRequestDTO request) {
        RegistroResponseDTO response = usuarioBusiness.registrar(request);
        return ResponseEntity.ok(ApiResponse.success("Usuario registrado exitosamente", response));
    }
}
