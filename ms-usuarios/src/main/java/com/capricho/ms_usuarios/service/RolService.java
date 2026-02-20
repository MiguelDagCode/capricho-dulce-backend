package com.capricho.ms_usuarios.service;

import com.capricho.ms_usuarios.model.Rol;

import java.util.List;
import java.util.Optional;

public interface RolService {
    List<Rol> listarTodos();
    Optional<Rol> buscarPorId(Integer idRol);
    Rol registrarRol(Rol rol);
    Rol actualizarRol(Integer idRol, Rol rol);
    void cambiarEstado(Integer idRol);
}