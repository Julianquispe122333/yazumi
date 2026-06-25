package com.example.apiyazumy.business;

import com.example.apiyazumy.dto.request.LoginRequestDTO;
import com.example.apiyazumy.dto.response.LoginResponseDTO;

public interface AuthBusiness {
    LoginResponseDTO login(LoginRequestDTO request);
}
