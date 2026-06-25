package com.example.apiyazumy.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDTO {
    @NotBlank(message = "El teléfono es obligatorio")
    @jakarta.validation.constraints.Size(max = 15, message = "El teléfono no puede exceder los 15 caracteres")
    private String telefono;
    
    @NotBlank(message = "El password es obligatorio")
    @jakarta.validation.constraints.Size(min = 6, message = "El password debe tener al menos 6 caracteres")
    private String password;
}
