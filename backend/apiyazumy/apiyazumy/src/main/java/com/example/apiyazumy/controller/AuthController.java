package com.example.apiyazumy.controller;

import com.example.apiyazumy.business.AuthBusiness;
import com.example.apiyazumy.dto.request.LoginRequestDTO;
import com.example.apiyazumy.dto.response.ApiResponse;
import com.example.apiyazumy.dto.response.LoginResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthBusiness authBusiness;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(@Valid @RequestBody LoginRequestDTO request) {
        LoginResponseDTO response = authBusiness.login(request);
        return ResponseEntity.ok(ApiResponse.success("Acceso permitido", response));
    }
}
