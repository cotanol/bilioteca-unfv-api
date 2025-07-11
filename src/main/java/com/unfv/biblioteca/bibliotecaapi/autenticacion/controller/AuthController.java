
package com.unfv.biblioteca.bibliotecaapi.autenticacion.controller;

import com.unfv.biblioteca.bibliotecaapi.autenticacion.dto.request.ActualizarUsuarioRequestDTO;
import com.unfv.biblioteca.bibliotecaapi.autenticacion.dto.request.CrearUsuarioRequestDTO;
import com.unfv.biblioteca.bibliotecaapi.autenticacion.dto.request.LoginRequestDTO;
import com.unfv.biblioteca.bibliotecaapi.autenticacion.dto.response.AuthResponseDTO;
import com.unfv.biblioteca.bibliotecaapi.autenticacion.dto.request.ActualizarTipoUsuarioRequestDTO;
import com.unfv.biblioteca.bibliotecaapi.autenticacion.dto.request.CrearTipoUsuarioRequestDTO;
import com.unfv.biblioteca.bibliotecaapi.autenticacion.dto.response.TipoUsuarioResponseDTO;
import com.unfv.biblioteca.bibliotecaapi.autenticacion.dto.response.UsuarioDetalleDTO;
import com.unfv.biblioteca.bibliotecaapi.autenticacion.service.AuthService;
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
    public ResponseEntity<UsuarioDetalleDTO> registrarUsuario(@Valid @RequestBody CrearUsuarioRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.crearUsuario(request));
    }

    @GetMapping("/usuarios")
    public ResponseEntity<List<UsuarioDetalleDTO>> listarUsuarios() {
        return ResponseEntity.ok(authService.findAllUsuarios());
    }

    @GetMapping("/usuarios/{id}")
    public ResponseEntity<UsuarioDetalleDTO> buscarUsuarioPorId(@PathVariable Long id) {
        return ResponseEntity.ok(authService.buscarUsuarioPorId(id));
    }

    @PutMapping("/usuarios/{id}")
    public ResponseEntity<UsuarioDetalleDTO> actualizarUsuario(@PathVariable Long id, @Valid @RequestBody ActualizarUsuarioRequestDTO request) {
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
        authService.eliminarTipoUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
