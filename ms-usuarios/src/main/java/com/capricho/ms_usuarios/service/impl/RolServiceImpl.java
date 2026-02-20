package com.capricho.ms_usuarios.service.impl;

import com.capricho.ms_usuarios.model.Rol;
import com.capricho.ms_usuarios.repository.RolRepository;
import com.capricho.ms_usuarios.service.RolService;
import com.capricho.ms_usuarios.util.BusinessException;
import com.capricho.ms_usuarios.util.ModeloNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class RolServiceImpl implements RolService{

	private final RolRepository rolRepository;
	
	@Override
	@Transactional(readOnly = true)
	public List<Rol> listarTodos() {
		return rolRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Rol> buscarPorId(Integer idRol) {
		return rolRepository.findById(idRol);
	}

	@Override
	@Transactional
	public Rol registrarRol(Rol rol) {
		String nombreRolFormat = rol.getNombreRol().trim().toUpperCase();

        if (rolRepository.existsByNombreRol(nombreRolFormat)) {
            throw new BusinessException("El rol '" + nombreRolFormat + "' ya se encuentra registrado.");
        }

        rol.setNombreRol(nombreRolFormat);
        rol.setActivo(true); 
        return rolRepository.save(rol);
	}

	@Override
	public Rol actualizarRol(Integer idRol, Rol rol) {
		Rol rolExistente = rolRepository.findById(idRol)
                .orElseThrow(() -> new ModeloNotFoundException("No se encontró el Rol con ID: " + idRol));

        String nombreRolFormat = rol.getNombreRol().trim().toUpperCase();

        // Validar que el nuevo nombre no esté siendo usado por OTRO rol diferente
        Optional<Rol> rolDuplicado = rolRepository.findByNombreRol(nombreRolFormat);
        if (rolDuplicado.isPresent() && !rolDuplicado.get().getIdRol().equals(idRol)) {
            throw new BusinessException("El nombre del rol '" + nombreRolFormat + "' ya está en uso.");
        }

        rolExistente.setNombreRol(nombreRolFormat);
        // No actualizamos el estado aquí, de eso se encarga el deleteLógico
        return rolRepository.save(rolExistente);
	}

	@Override
	public void cambiarEstado(Integer idRol) {
		Rol rol = rolRepository.findById(idRol)
                .orElseThrow(() -> new ModeloNotFoundException("No se encontró el Rol con ID: " + idRol));

        // Invertimos el estado actual (Toggle)
        rol.setActivo(!rol.getActivo());
        rolRepository.save(rol);
	}

}
