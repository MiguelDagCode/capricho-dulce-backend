package com.capricho.ms_usuarios.service.impl;

import com.capricho.ms_usuarios.dto.request.TrabajadorCreateDto;
import com.capricho.ms_usuarios.dto.request.UsuarioUpdateDto;
import com.capricho.ms_usuarios.dto.response.UsuarioListResponseDto;
import com.capricho.ms_usuarios.model.Cliente;
import com.capricho.ms_usuarios.model.Rol;
import com.capricho.ms_usuarios.model.Trabajador;
import com.capricho.ms_usuarios.model.Usuario;
import com.capricho.ms_usuarios.repository.RolRepository;
import com.capricho.ms_usuarios.repository.TrabajadorRepository;
import com.capricho.ms_usuarios.repository.UsuarioRepository;
import com.capricho.ms_usuarios.service.UsuarioService;
import com.capricho.ms_usuarios.util.BusinessException;
import com.capricho.ms_usuarios.util.ModeloNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final TrabajadorRepository trabajadorRepository;
    // --- MÉTODOS INTERNOS ---
    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    // --- MÉTODOS PARA CONTROLADORES ---
    @Override
    @Transactional(readOnly = true)
    public List<UsuarioListResponseDto> findAll() {
        return usuarioRepository.findAll().stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UsuarioListResponseDto> findById(Long idUsuario) {
        return usuarioRepository.findById(idUsuario)
                .map(this::mapearADto);
    }

    @Override
    @Transactional
    public void deleteLogico(Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ModeloNotFoundException("Usuario no encontrado con ID: " + idUsuario));

        usuario.setActivo(!usuario.getActivo());
        usuarioRepository.save(usuario);
    }

    // --- MÉTODO AUXILIAR DE MAPEO ---
    private UsuarioListResponseDto mapearADto(Usuario u) {
        UsuarioListResponseDto dto = new UsuarioListResponseDto();
        dto.setIdUsuario(u.getIdUsuario());
        dto.setCorreo(u.getEmail());
        dto.setNombreRol(u.getRol().getNombreRol());
        dto.setEstado(u.getActivo());

        // Extraemos nombres y apellidos de las clases hijas gracias a InheritanceType.JOINED
        if (u instanceof Trabajador t) {
            dto.setNombre(t.getNombres());
            dto.setApellido(t.getApellidos());
            dto.setTelefono(t.getCelular());
        } else if (u instanceof Cliente c) {
            dto.setNombre(c.getNombres());
            dto.setApellido(c.getApellidos());
            dto.setTelefono(c.getCelular());
        }
        return dto;
    }

	@Override
	@Transactional
	public UsuarioListResponseDto actualizar(Long idUsuario, UsuarioUpdateDto request) {
		Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ModeloNotFoundException("Usuario no encontrado con ID: " + idUsuario));

        // Validar correo duplicado si es que lo está cambiando
        if (request.getEmail() != null && !usuario.getEmail().equalsIgnoreCase(request.getEmail())) {
            if (usuarioRepository.existsByEmail(request.getEmail())) {
                throw new BusinessException("El correo '" + request.getEmail() + "' ya está registrado.");
            }
            usuario.setEmail(request.getEmail());
        }

        // Validar y actualizar Rol
        if (request.getIdRol() != null) {
            Rol rol = rolRepository.findById(request.getIdRol())
                    .orElseThrow(() -> new ModeloNotFoundException("Rol no encontrado con ID: " + request.getIdRol()));
            usuario.setRol(rol);
        }

        // Actualizar contraseña solo si se envía una nueva
        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        Usuario actualizado = usuarioRepository.save(usuario);
        return mapearADto(actualizado);
	}

	@Override
	@Transactional
	public Trabajador registrarTrabajador(TrabajadorCreateDto request) {
		// 1. Validaciones de negocio
	    if (usuarioRepository.existsByEmail(request.getEmail())) {
	        throw new BusinessException("El correo '" + request.getEmail() + "' ya se encuentra registrado.");
	    }
	    
	    if (trabajadorRepository.existsByDocumento(request.getDocumento())) {
	        throw new BusinessException("El documento '" + request.getDocumento() + "' ya está asociado a otro trabajador.");
	    }

	    // 2. Buscar el Rol
	    Rol rol = rolRepository.findById(request.getIdRol())
	            .orElseThrow(() -> new ModeloNotFoundException("No se encontró el Rol con ID: " + request.getIdRol()));

	    // 3. Crear el objeto Trabajador
	    Trabajador trabajador = new Trabajador();
	    
	    // -> Datos heredados de Usuario
	    trabajador.setEmail(request.getEmail());
	    trabajador.setPassword(passwordEncoder.encode(request.getPassword())); // Encriptación clave
	    trabajador.setRol(rol);
	    trabajador.setActivo(true);
	    
	    // -> Datos propios de Trabajador
	    trabajador.setNombres(request.getNombres());
	    trabajador.setApellidos(request.getApellidos());
	    trabajador.setCelular(request.getCelular());
	    trabajador.setTipoDocumento(request.getTipoDocumento());
	    trabajador.setDocumento(request.getDocumento());
	    trabajador.setSalario(request.getSalario());

	    // 4. Guardar (JPA insertará en tb_usuario y tb_trabajador automáticamente)
	    return trabajadorRepository.save(trabajador);
	}
}