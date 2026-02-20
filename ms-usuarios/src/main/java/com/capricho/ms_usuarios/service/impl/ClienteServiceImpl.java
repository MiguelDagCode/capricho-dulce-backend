package com.capricho.ms_usuarios.service.impl;

import com.capricho.ms_usuarios.dto.request.RegistroClienteRequest;
import com.capricho.ms_usuarios.dto.request.UpdatePerfilRequest;
import com.capricho.ms_usuarios.model.Cliente;
import com.capricho.ms_usuarios.model.Distrito;
import com.capricho.ms_usuarios.model.Rol;
import com.capricho.ms_usuarios.repository.ClienteRepository;
import com.capricho.ms_usuarios.repository.DistritoRepository;
import com.capricho.ms_usuarios.repository.RolRepository;
import com.capricho.ms_usuarios.repository.UsuarioRepository;
import com.capricho.ms_usuarios.service.ClienteService;
import com.capricho.ms_usuarios.util.BusinessException;
import com.capricho.ms_usuarios.util.ModeloNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;
    private final RolRepository rolRepository;
    private final DistritoRepository distritoRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Cliente registrarse(RegistroClienteRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("El correo electrónico ya está registrado.");
        }
        if (clienteRepository.existsByDocumento(request.getDocumento())) {
            throw new BusinessException("El documento de identidad ya está asociado a otra cuenta.");
        }

        // Asignación segura de rol: El backend decide que es cliente, no el frontend
        Rol rolCliente = rolRepository.findByNombreRol("ROLE_CLIENTE")
                .orElseThrow(() -> new BusinessException("Error de configuración: Rol de cliente no encontrado."));

        Distrito distrito = distritoRepository.findById(request.getIdDistrito())
                .orElseThrow(() -> new ModeloNotFoundException("Distrito no encontrado"));

        Cliente cliente = new Cliente();
        cliente.setEmail(request.getEmail());
        cliente.setPassword(passwordEncoder.encode(request.getPassword()));
        cliente.setRol(rolCliente);
        cliente.setActivo(true);
        
        cliente.setNombres(request.getNombres());
        cliente.setApellidos(request.getApellidos());
        cliente.setCelular(request.getCelular());
        cliente.setDireccionReferencia(request.getDireccionReferencia());
        cliente.setDistrito(distrito);
        cliente.setTipoDocumento(request.getTipoDocumento());
        cliente.setDocumento(request.getDocumento());

        return clienteRepository.save(cliente);
    }

    @Override
    @Transactional
    public Cliente actualizarMiPerfil(Integer idUsuario, UpdatePerfilRequest request) {
        Cliente cliente = clienteRepository.findById(idUsuario)
                .orElseThrow(() -> new ModeloNotFoundException("No se encontró el perfil del cliente."));

        Distrito distrito = distritoRepository.findById(request.getIdDistrito())
                .orElseThrow(() -> new ModeloNotFoundException("Distrito no encontrado"));

        cliente.setNombres(request.getNombres());
        cliente.setApellidos(request.getApellidos());
        cliente.setCelular(request.getCelular());
        cliente.setDireccionReferencia(request.getDireccionReferencia());
        cliente.setDistrito(distrito);

        return clienteRepository.save(cliente);
    }

    @Override
    @Transactional
    public void desactivarMiCuenta(Integer idUsuario) {
        Cliente cliente = clienteRepository.findById(idUsuario)
                .orElseThrow(() -> new ModeloNotFoundException("No se encontró la cuenta."));
        
        cliente.setActivo(false); // Solo desactivar, no toggle. El cliente se da de baja.
        clienteRepository.save(cliente);
    }
}