package com.example.apiyazumy.business;

import com.example.apiyazumy.dto.request.RegistroRequestDTO;
import com.example.apiyazumy.dto.response.RegistroResponseDTO;

public interface UsuarioBusiness {
    RegistroResponseDTO registrar(RegistroRequestDTO request);
}
