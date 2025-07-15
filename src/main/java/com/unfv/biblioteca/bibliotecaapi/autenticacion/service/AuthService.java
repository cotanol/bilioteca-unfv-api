
package com.unfv.biblioteca.bibliotecaapi.autenticacion.service;

import com.unfv.biblioteca.bibliotecaapi.autenticacion.domain.TipoUsuario;
import com.unfv.biblioteca.bibliotecaapi.autenticacion.domain.Usuario;
import com.unfv.biblioteca.bibliotecaapi.autenticacion.dto.request.ActualizarUsuarioRequestDTO;
import com.unfv.biblioteca.bibliotecaapi.autenticacion.dto.request.CrearUsuarioRequestDTO;
import com.unfv.biblioteca.bibliotecaapi.autenticacion.dto.request.LoginRequestDTO;
import com.unfv.biblioteca.bibliotecaapi.autenticacion.dto.response.*;
import com.unfv.biblioteca.bibliotecaapi.autenticacion.mapper.AuthMapper;
import com.unfv.biblioteca.bibliotecaapi.autenticacion.repository.TipoUsuarioRepository;
import com.unfv.biblioteca.bibliotecaapi.autenticacion.repository.UsuarioRepository;
import com.unfv.biblioteca.bibliotecaapi.autenticacion.dto.request.ActualizarTipoUsuarioRequestDTO;
import com.unfv.biblioteca.bibliotecaapi.autenticacion.dto.request.CrearTipoUsuarioRequestDTO;
import com.unfv.biblioteca.bibliotecaapi.catalogo.repository.*;
import com.unfv.biblioteca.bibliotecaapi.circulacion.dto.response.MultaReponseDTO;
import com.unfv.biblioteca.bibliotecaapi.circulacion.dto.response.PrestamoResponseDTO;
import com.unfv.biblioteca.bibliotecaapi.circulacion.repository.MultaRepository;
import com.unfv.biblioteca.bibliotecaapi.circulacion.repository.PrestamoRepository;
import com.unfv.biblioteca.bibliotecaapi.circulacion.service.MultaService;
import com.unfv.biblioteca.bibliotecaapi.circulacion.service.PrestamoService;
import com.unfv.biblioteca.bibliotecaapi.reserva.dto.response.ReservaResponseDTO;
import com.unfv.biblioteca.bibliotecaapi.reserva.repository.ReservaRepository;
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
    private final AutorRepository autorRepository;
    private final CategoriaRepository categoriaRepository;
    private final MaterialRepository materialRepository;
    private final UbicacionRepository ubicacionRepository;
    private final EditorialRepository editorialRepository;
    private final EjemplarRepository ejemplarRepository;
    private final PrestamoRepository prestamoRepository;
    private final MultaRepository multaRepository;
    private final ReservaRepository reservaRepository;

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

        UsuarioResponseDTO usuarioDto = authMapper.toUsuarioDetalleDTO(usuario);

        return AuthResponseDTO.builder()
                .token(fakeToken)
                .usuario(usuarioDto)
                .build();
    }

    @Transactional
    public UsuarioResponseDTO crearUsuario(CrearUsuarioRequestDTO request) {
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
    public List<UsuarioResponseDTO> findAllUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(authMapper::toUsuarioDetalleDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UsuarioResponseDTO buscarUsuarioPorId(Long id) {
        return usuarioRepository.findById(id)
                .map(authMapper::toUsuarioDetalleDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }

    @Transactional
    public UsuarioResponseDTO actualizarUsuario(Long id, ActualizarUsuarioRequestDTO request) {
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
        UsuarioResponseDTO usuarioDto = buscarUsuarioPorId(usuarioId);
        List<PrestamoResponseDTO> prestamos = prestamoService.buscarPrestamosActivosPorUsuario(usuarioId);
        List<ReservaResponseDTO> reservas = reservaService.buscarReservasActivasPorUsuario(usuarioId);
        List<MultaReponseDTO> multas = multaService.buscarMultasPendientesPorUsuario(usuarioId);

        return PerfilResponseDTO.builder()
                .datosUsuario(usuarioDto)
                .prestamosActivos(prestamos)
                .reservasActivas(reservas)
                .multasPendientes(multas)
                .build();
    }

    @Transactional(readOnly = true)
    public List<PrestamoResponseDTO> getHistorialPrestamosUsuario(Long usuarioId) {
        return prestamoService.buscarHistorialPrestamosPorUsuario(usuarioId);
    }

    @Transactional
    public void actualizarEstadoUsuario(Long id, String nuevoEstado) {
        // 1. Buscar el usuario o lanzar un error si no existe
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));

        // 2. Validar que el estado sea uno de los permitidos (opcional pero muy recomendado)
        // Por ejemplo:
        List<String> estadosValidos = List.of("Activo", "Suspendido", "Inactivo");
        if (!estadosValidos.contains(nuevoEstado)) {
            throw new BusinessRuleException("El estado '" + nuevoEstado + "' no es válido.");
        }

        // 3. Asignar el nuevo estado
        usuario.setEstado(nuevoEstado);

        // 4. Guardar los cambios en la base de datos
        // Como el método es @Transactional, JPA/Hibernate guardará los cambios automáticamente
        // al final de la transacción, pero un save() explícito también es correcto.
        usuarioRepository.save(usuario);
    }

    @Transactional(readOnly = true) // Buena práctica para operaciones de solo lectura
    public DashboardStatsDTO getDashboardStats() {
        // Usamos el patrón Builder del DTO para construir la respuesta
        return DashboardStatsDTO.builder()
                .cantidadAutores(autorRepository.count())
                .cantidadCategorias(categoriaRepository.count())
                .cantidadEditoriales(editorialRepository.count())
                .cantidadMateriales(materialRepository.count())
                .cantidadUbicaciones(ubicacionRepository.count())
                .cantidadEjemplares(ejemplarRepository.count())
                .cantidadUsuarios(usuarioRepository.count())
                .cantidadPrestamos(prestamoRepository.count())
                .cantidadMultas(multaRepository.count())
                .cantidadReservas(reservaRepository.count())
                .build();
    }
}
