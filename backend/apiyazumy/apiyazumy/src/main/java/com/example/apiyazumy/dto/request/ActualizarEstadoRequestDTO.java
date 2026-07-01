package com.example.apiyazumy.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActualizarEstadoRequestDTO {

    @NotNull(message = "El idEstado es obligatorio")
    private Integer idEstado;
}
