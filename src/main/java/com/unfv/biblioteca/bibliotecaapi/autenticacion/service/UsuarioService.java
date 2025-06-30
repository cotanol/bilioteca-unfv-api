package com.unfv.biblioteca.bibliotecaapi.autenticacion.service;

import com.unfv.biblioteca.bibliotecaapi.autenticacion.domain.TipoUsuario;
import com.unfv.biblioteca.bibliotecaapi.autenticacion.domain.Usuario;
import com.unfv.biblioteca.bibliotecaapi.autenticacion.dto.request.CrearUsuarioRequestDTO;
import com.unfv.biblioteca.bibliotecaapi.autenticacion.dto.response.UsuarioDetalleDTO;
import com.unfv.biblioteca.bibliotecaapi.autenticacion.mapper.UsuarioMapper;
import com.unfv.biblioteca.bibliotecaapi.autenticacion.repository.TipoUsuarioRepository;
import com.unfv.biblioteca.bibliotecaapi.autenticacion.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor // Alternativa a la inyección por constructor manual
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final TipoUsuarioRepository tipoUsuarioRepository;
    private final UsuarioMapper usuarioMapper;

    @Transactional
    public UsuarioDetalleDTO crearUsuario(CrearUsuarioRequestDTO request) {
        // 1. Validación de negocio simple
        if (usuarioRepository.existsByDni(request.getDni())) {
            throw new IllegalStateException("El DNI ya está registrado.");
        }
        if (usuarioRepository.existsByCodigoUniversitario(request.getCodigoUniversitario())) {
            throw new IllegalStateException("El código universitario ya está registrado.");
        }
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new IllegalStateException("El email ya está registrado.");
        }

        // 2. Buscamos el tipo de usuario
        TipoUsuario tipoUsuario = tipoUsuarioRepository.findById(request.getTipoUsuarioId())
                .orElseThrow(() -> new RuntimeException("Tipo de usuario no encontrado"));

        // 3. Creamos la nueva entidad Usuario
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setCodigoUniversitario(request.getCodigoUniversitario());
        nuevoUsuario.setDni(request.getDni());
        nuevoUsuario.setNombres(request.getNombres());
        nuevoUsuario.setApellidoPaterno(request.getApellidoPaterno());
        nuevoUsuario.setApellidoMaterno(request.getApellidoMaterno());
        nuevoUsuario.setEmail(request.getEmail());
        nuevoUsuario.setTelefono(request.getTelefono());
        nuevoUsuario.setTipoUsuario(tipoUsuario);
        nuevoUsuario.setEstado("Activo");
        nuevoUsuario.setFechaRegistro(LocalDateTime.now());

        // ¡¡¡ADVERTENCIA: SOLO PARA PRUEBAS Y DEMOSTRACIÓN!!!
        // En un entorno real, aquí se "hashea" la contraseña con BCrypt.
        // Por ahora, la guardamos en texto plano para que el login simple funcione.
        nuevoUsuario.setPasswordHash(request.getPassword());

        // 4. Guardamos el usuario
        Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);

        // 5. Devolvemos el DTO de respuesta
        return usuarioMapper.toUsuarioDetalleDTO(usuarioGuardado);
    }

    // Aquí podrías añadir un metodo para buscar un usuario por ID, etc.
    @Transactional(readOnly = true)
    public UsuarioDetalleDTO buscarUsuarioPorId(Long id) {
        return usuarioRepository.findById(id)
                .map(usuarioMapper::toUsuarioDetalleDTO)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

}
