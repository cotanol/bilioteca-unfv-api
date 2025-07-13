
package com.unfv.biblioteca.bibliotecaapi.autenticacion.service;

import com.unfv.biblioteca.bibliotecaapi.autenticacion.domain.TipoUsuario;
import com.unfv.biblioteca.bibliotecaapi.autenticacion.domain.Usuario;
import com.unfv.biblioteca.bibliotecaapi.autenticacion.dto.request.ActualizarUsuarioRequestDTO;
import com.unfv.biblioteca.bibliotecaapi.autenticacion.dto.request.CrearUsuarioRequestDTO;
import com.unfv.biblioteca.bibliotecaapi.autenticacion.dto.request.LoginRequestDTO;
import com.unfv.biblioteca.bibliotecaapi.autenticacion.dto.response.AuthResponseDTO;
import com.unfv.biblioteca.bibliotecaapi.autenticacion.dto.response.PerfilResponseDTO;
import com.unfv.biblioteca.bibliotecaapi.autenticacion.dto.response.UsuarioDetalleDTO;
import com.unfv.biblioteca.bibliotecaapi.autenticacion.mapper.AuthMapper;
import com.unfv.biblioteca.bibliotecaapi.autenticacion.repository.TipoUsuarioRepository;
import com.unfv.biblioteca.bibliotecaapi.autenticacion.repository.UsuarioRepository;
import com.unfv.biblioteca.bibliotecaapi.autenticacion.dto.request.ActualizarTipoUsuarioRequestDTO;
import com.unfv.biblioteca.bibliotecaapi.autenticacion.dto.request.CrearTipoUsuarioRequestDTO;
import com.unfv.biblioteca.bibliotecaapi.autenticacion.dto.response.TipoUsuarioResponseDTO;
import com.unfv.biblioteca.bibliotecaapi.circulacion.dto.response.MultaDTO;
import com.unfv.biblioteca.bibliotecaapi.circulacion.dto.response.PrestamoDetalleDTO;
import com.unfv.biblioteca.bibliotecaapi.circulacion.service.MultaService;
import com.unfv.biblioteca.bibliotecaapi.circulacion.service.PrestamoService;
import com.unfv.biblioteca.bibliotecaapi.reserva.dto.response.ReservaDetalleDTO;
import com.unfv.biblioteca.bibliotecaapi.reserva.service.ReservaService;
import com.unfv.biblioteca.bibliotecaapi.shared.exception.BusinessRuleException;
import com.unfv.biblioteca.bibliotecaapi.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final TipoUsuarioRepository tipoUsuarioRepository;
    private final AuthMapper authMapper;
    private final PrestamoService prestamoService;
    private final MultaService multaService;
    private final ReservaService reservaService;

    // Métodos para Usuario
    @Transactional
    public AuthResponseDTO login(LoginRequestDTO request) {
        Usuario usuario = usuarioRepository.findByCodigoUniversitario(request.getCodigoUniversitario())
                .orElseThrow(() -> new ResourceNotFoundException("Credenciales incorrectas"));

        // Inyectar Passsword Encoder seria lo mejor (futuro)
        if (!usuario.getPasswordHash().equals(request.getPassword())) {
            throw new ResourceNotFoundException("Credenciales incorrectas");
        }

        String fakeToken = "fake-jwt-token-for-" + usuario.getEmail();

        UsuarioDetalleDTO usuarioDto = authMapper.toUsuarioDetalleDTO(usuario);

        return AuthResponseDTO.builder()
                .token(fakeToken)
                .usuario(usuarioDto)
                .build();
    }

    @Transactional
    public UsuarioDetalleDTO crearUsuario(CrearUsuarioRequestDTO request) {
        if (usuarioRepository.existsByDni(request.getDni())) {
            throw new BusinessRuleException("El DNI ya está registrado.");
        }
        if (usuarioRepository.existsByCodigoUniversitario(request.getCodigoUniversitario())) {
            throw new BusinessRuleException("El código universitario ya está registrado.");
        }
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new BusinessRuleException("El email ya está registrado.");
        }

        TipoUsuario tipoUsuario = tipoUsuarioRepository.findById(request.getTipoUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de usuario no encontrado"));

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
        nuevoUsuario.setPasswordHash(request.getPassword());

        Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);

        return authMapper.toUsuarioDetalleDTO(usuarioGuardado);
    }

    @Transactional(readOnly = true)
    public List<UsuarioDetalleDTO> findAllUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(authMapper::toUsuarioDetalleDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UsuarioDetalleDTO buscarUsuarioPorId(Long id) {
        return usuarioRepository.findById(id)
                .map(authMapper::toUsuarioDetalleDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }

    @Transactional
    public UsuarioDetalleDTO actualizarUsuario(Long id, ActualizarUsuarioRequestDTO request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        authMapper.updateUserFromDto(request, usuario);
        Usuario usuarioActualizado = usuarioRepository.save(usuario);

        return authMapper.toUsuarioDetalleDTO(usuarioActualizado);
    }

    @Transactional
    public void eliminarUsuario(Long id) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));

        usuario.setEstado("Inactivo");
        usuarioRepository.save(usuario);
        //usuarioRepository.deleteById(id);
    }

    // Métodos para TipoUsuario
    @Transactional(readOnly = true)
    public List<TipoUsuarioResponseDTO> findAllTiposUsuario() {
        return tipoUsuarioRepository.findAll().stream()
                .map(authMapper::toTipoUsuarioResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TipoUsuarioResponseDTO findTipoUsuarioById(Long id) {
        TipoUsuario tipoUsuario = tipoUsuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de usuario no encontrado con ID: " + id));
        return authMapper.toTipoUsuarioResponseDTO(tipoUsuario);
    }

    @Transactional
    public TipoUsuarioResponseDTO crearTipoUsuario(CrearTipoUsuarioRequestDTO request) {
        if (tipoUsuarioRepository.existsByNombreTipo(request.getNombreTipo())) {
            throw new BusinessRuleException("El tipo de usuario '" + request.getNombreTipo() + "' ya existe.");
        }
        TipoUsuario nuevoTipoUsuario = authMapper.toTipoUsuario(request);
        TipoUsuario tipoUsuarioGuardado = tipoUsuarioRepository.save(nuevoTipoUsuario);
        return authMapper.toTipoUsuarioResponseDTO(tipoUsuarioGuardado);
    }

    @Transactional
    public TipoUsuarioResponseDTO actualizarTipoUsuario(Long id, ActualizarTipoUsuarioRequestDTO request) {
        TipoUsuario tipoUsuarioExistente = tipoUsuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de usuario no encontrado con ID: " + id));
        authMapper.updateTipoUsuarioFromDto(request, tipoUsuarioExistente);
        TipoUsuario tipoUsuarioActualizado = tipoUsuarioRepository.save(tipoUsuarioExistente);
        return authMapper.toTipoUsuarioResponseDTO(tipoUsuarioActualizado);
    }

    @Transactional
    public void eliminarTipoUsuario(Long id) {
        if (!tipoUsuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Tipo de usuario no encontrado con ID: " + id);
        }
        tipoUsuarioRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public PerfilResponseDTO obtenerPerfilUsuarioLogueado(Long usuarioId) {
        UsuarioDetalleDTO usuarioDto = buscarUsuarioPorId(usuarioId);
        List<PrestamoDetalleDTO> prestamos = prestamoService.buscarPrestamosActivosPorUsuario(usuarioId);
        List<ReservaDetalleDTO> reservas = reservaService.buscarReservasActivasPorUsuario(usuarioId);
        List<MultaDTO> multas = multaService.buscarMultasPendientesPorUsuario(usuarioId);

        return PerfilResponseDTO.builder()
                .datosUsuario(usuarioDto)
                .prestamosActivos(prestamos)
                .reservasActivas(reservas)
                .multasPendientes(multas)
                .build();
    }

    @Transactional(readOnly = true)
    public List<PrestamoDetalleDTO> getHistorialPrestamosUsuario(Long usuarioId) {
        return prestamoService.buscarHistorialPrestamosPorUsuario(usuarioId);
    }
}
