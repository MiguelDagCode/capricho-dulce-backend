package com.capricho.ms_usuarios.service;

import com.capricho.ms_usuarios.dto.request.LoginRequest;
import com.capricho.ms_usuarios.dto.response.AuthResponse;

public interface AuthService {
	AuthResponse login(LoginRequest request);
}
