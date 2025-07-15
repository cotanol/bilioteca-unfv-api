
package com.unfv.biblioteca.bibliotecaapi.autenticacion.controller;

import com.unfv.biblioteca.bibliotecaapi.autenticacion.dto.request.*;
import com.unfv.biblioteca.bibliotecaapi.autenticacion.dto.response.*;
import com.unfv.biblioteca.bibliotecaapi.autenticacion.service.AuthService;
import com.unfv.biblioteca.bibliotecaapi.circulacion.dto.response.PrestamoResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // Endpoints de Autenticación y Usuarios
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/registrar")
    public ResponseEntity<UsuarioResponseDTO> registrarUsuario(@Valid @RequestBody CrearUsuarioRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.crearUsuario(request));
    }

    @GetMapping("/usuarios")
    public ResponseEntity<List<UsuarioResponseDTO>> listarUsuarios() {
        return ResponseEntity.ok(authService.findAllUsuarios());
    }

    @GetMapping("/admin/dashboard")
    public ResponseEntity<DashboardStatsDTO> obtenerEstadisticasDashboard() {
        return ResponseEntity.ok(authService.getDashboardStats());
    }

    @GetMapping("/usuarios/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarUsuarioPorId(@PathVariable Long id) {
        return ResponseEntity.ok(authService.buscarUsuarioPorId(id));
    }

    @PutMapping("/usuarios/{id}")
    public ResponseEntity<UsuarioResponseDTO> actualizarUsuario(@PathVariable Long id, @Valid @RequestBody ActualizarUsuarioRequestDTO request) {
        return ResponseEntity.ok(authService.actualizarUsuario(id, request));
    }

    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        authService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoints de TipoUsuario
    @GetMapping("/tipos-usuario")
    public ResponseEntity<List<TipoUsuarioResponseDTO>> listarTiposUsuario() {
        return ResponseEntity.ok(authService.findAllTiposUsuario());
    }

    @GetMapping("/tipos-usuario/{id}")
    public ResponseEntity<TipoUsuarioResponseDTO> buscarTipoUsuarioPorId(@PathVariable Long id) {
        return ResponseEntity.ok(authService.findTipoUsuarioById(id));
    }

    @PostMapping("/tipos-usuario")
    public ResponseEntity<TipoUsuarioResponseDTO> crearTipoUsuario(@Valid @RequestBody CrearTipoUsuarioRequestDTO request) {
        TipoUsuarioResponseDTO tipoUsuarioCreado = authService.crearTipoUsuario(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(tipoUsuarioCreado);
    }

    @PutMapping("/tipos-usuario/{id}")
    public ResponseEntity<TipoUsuarioResponseDTO> actualizarTipoUsuario(@PathVariable Long id, @Valid @RequestBody ActualizarTipoUsuarioRequestDTO request) {
        return ResponseEntity.ok(authService.actualizarTipoUsuario(id, request));
    }

    @DeleteMapping("/tipos-usuario/{id}")
    public ResponseEntity<Void> eliminarTipoUsuario(@PathVariable Long id) {
        authService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/perfil/{id}")
    public ResponseEntity<PerfilResponseDTO> obtenerPerfil(@PathVariable Long id) {
        return ResponseEntity.ok(authService.obtenerPerfilUsuarioLogueado(id));
    }

    @GetMapping("/perfil/historial/{id}")
    public ResponseEntity<List<PrestamoResponseDTO>> obtenerHistorialPrestamos(@PathVariable Long id) {
        return ResponseEntity.ok(authService.getHistorialPrestamosUsuario(id));
    }

    @PatchMapping("/usuarios/{id}/estado")
    public ResponseEntity<Void> actualizarEstadoUsuario(@PathVariable Long id, @Valid @RequestBody ActualizarEstadoUsuarioRequestDTO request) {
        authService.actualizarEstadoUsuario(id, request.getEstado());
        return ResponseEntity.noContent().build(); // Retorna 204 No Content, ideal para actualizaciones parciales
    }


}
