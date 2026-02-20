package com.capricho.ms_usuarios.service;

import com.capricho.ms_usuarios.dto.request.RegistroClienteRequest;
import com.capricho.ms_usuarios.dto.request.UpdatePerfilRequest;
import com.capricho.ms_usuarios.model.Cliente;

public interface ClienteService {
    Cliente registrarse(RegistroClienteRequest request);
    Cliente actualizarMiPerfil(Integer idUsuario, UpdatePerfilRequest request);
    void desactivarMiCuenta(Integer idUsuario);
}