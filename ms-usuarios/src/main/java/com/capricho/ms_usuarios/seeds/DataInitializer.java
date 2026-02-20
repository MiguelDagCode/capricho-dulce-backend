package com.capricho.ms_usuarios.seeds;

import com.capricho.ms_usuarios.enums.TipoDocumento;
import com.capricho.ms_usuarios.model.Cliente;
import com.capricho.ms_usuarios.model.Distrito;
import com.capricho.ms_usuarios.model.Rol;
import com.capricho.ms_usuarios.model.Trabajador;
import com.capricho.ms_usuarios.repository.ClienteRepository;
import com.capricho.ms_usuarios.repository.DistritoRepository;
import com.capricho.ms_usuarios.repository.RolRepository;
import com.capricho.ms_usuarios.repository.TrabajadorRepository;
import com.capricho.ms_usuarios.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository; 
    private final TrabajadorRepository trabajadorRepository;
    private final ClienteRepository clienteRepository;
    private final DistritoRepository distritoRepository;
    private final PasswordEncoder passwordEncoder;

    // Configuración de usuarios por defecto
    @Value("${app.admin.email:admin@capricho.com}")
    private String adminEmail;
    @Value("${app.admin.password:admin}")
    private String adminPassword;

    @Value("${app.pastelero.email:pastelero@capricho.com}")
    private String pasteleroEmail;
    @Value("${app.pastelero.password:pastelero}")
    private String pasteleroPassword;
    
    // Agregamos un cliente de prueba
    @Value("${app.cliente.email:cliente@capricho.com}")
    private String clienteEmail;
    @Value("${app.cliente.password:cliente}")
    private String clientePassword;

    @Override
    @Transactional
    public void run(String... args) {
        log.info("--- INICIANDO CARGA DE DATOS MAESTROS ---");

        // 1. Crear distritos (Zona Lima Norte)
        Distrito distCarabayllo = crearDistritoSiNoExiste("Carabayllo", new BigDecimal("8.00"));
        Distrito distComas = crearDistritoSiNoExiste("Comas", new BigDecimal("10.00"));
        Distrito distLosOlivos = crearDistritoSiNoExiste("Los Olivos", new BigDecimal("12.00"));
        Distrito distIndependencia = crearDistritoSiNoExiste("Independencia", new BigDecimal("14.00"));
        Distrito distSmp = crearDistritoSiNoExiste("San Martín de Porres", new BigDecimal("15.00"));

        // 2. Crear roles del sistema
        Rol rolAdmin = crearRolSiNoExiste("ADMIN");
        Rol rolPastelero = crearRolSiNoExiste("PASTELERO");
        Rol rolCliente = crearRolSiNoExiste("CLIENTE");

        // 3. Crear personal interno
        crearTrabajadorSiNoExiste("Miguel", "Administrador", adminEmail, adminPassword, "11111111", rolAdmin);
        crearTrabajadorSiNoExiste("Pedro", "Horno", pasteleroEmail, pasteleroPassword, "33333333", rolPastelero);

        // 4. Crear cliente de prueba
        crearClienteSiNoExiste("Carlos", "Comprador", clienteEmail, clientePassword, "44444444", rolCliente, distCarabayllo);

        log.info("--- CARGA DE DATOS MAESTROS FINALIZADA ---");
    }

    private Distrito crearDistritoSiNoExiste(String nombre, BigDecimal costoEnvio) {
        // Asume un método findByNombre en DistritoRepository
        return distritoRepository.findByNombre(nombre)
                .orElseGet(() -> {
                    Distrito distrito = new Distrito();
                    distrito.setNombre(nombre);
                    distrito.setCostoEnvio(costoEnvio);
                    distrito.setActivo(true);
                    log.info(" > Creando distrito: {}", nombre);
                    return distritoRepository.save(distrito);
                });
    }

    private Rol crearRolSiNoExiste(String nombreRol) {
        return rolRepository.findByNombreRol(nombreRol)
                .orElseGet(() -> {
                    Rol rol = new Rol();
                    rol.setNombreRol(nombreRol);
                    rol.setActivo(true);
                    log.info(" > Creando rol: {}", nombreRol);
                    return rolRepository.save(rol);
                });
    }

    private void crearTrabajadorSiNoExiste(String nombres, String apellidos, String email, String password, String dni, Rol rol) {
        if (usuarioRepository.existsByEmail(email)) {
            log.info(" > El usuario ya existe ({}). No se realizaron cambios.", email);
        } else {
            Trabajador trabajador = new Trabajador();
            
            trabajador.setEmail(email);
            trabajador.setPassword(passwordEncoder.encode(password));
            trabajador.setRol(rol);
            trabajador.setActivo(true);
            
            trabajador.setNombres(nombres);
            trabajador.setApellidos(apellidos);
            trabajador.setCelular("999999999");
            trabajador.setTipoDocumento(TipoDocumento.DNI);
            trabajador.setDocumento(dni);
            trabajador.setSalario(new BigDecimal("1500.00"));

            trabajadorRepository.save(trabajador);
            log.info(" > Usuario {} creado con éxito: {}", rol.getNombreRol(), email);
        }
    }
    
    private void crearClienteSiNoExiste(String nombres, String apellidos, String email, String password, String dni, Rol rol, Distrito distrito) {
        if (usuarioRepository.existsByEmail(email)) {
            log.info(" > El cliente ya existe ({}). No se realizaron cambios.", email);
        } else {
            Cliente cliente = new Cliente(); // ¡Corrección aquí!
            
            // Campos heredados de Usuario
            cliente.setEmail(email);
            cliente.setPassword(passwordEncoder.encode(password));
            cliente.setRol(rol);
            cliente.setActivo(true);
            
            // Campos específicos de Cliente
            cliente.setNombres(nombres);
            cliente.setApellidos(apellidos);
            cliente.setCelular("988888888");
            cliente.setTipoDocumento(TipoDocumento.DNI);
            cliente.setDocumento(dni);
            cliente.setDistrito(distrito);
            cliente.setDireccionReferencia("Av. Tupac Amaru Mz A Lote 1");

            clienteRepository.save(cliente); // Guardamos en el repo de cliente
            log.info(" > Usuario {} creado con éxito: {}", rol.getNombreRol(), email);
        }
    }
}