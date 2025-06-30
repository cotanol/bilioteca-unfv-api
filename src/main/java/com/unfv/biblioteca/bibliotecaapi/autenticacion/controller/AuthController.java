package com.unfv.biblioteca.bibliotecaapi.autenticacion.controller;

import com.unfv.biblioteca.bibliotecaapi.autenticacion.dto.request.CrearUsuarioRequestDTO;
import com.unfv.biblioteca.bibliotecaapi.autenticacion.dto.request.LoginRequestDTO;
import com.unfv.biblioteca.bibliotecaapi.autenticacion.dto.response.AuthResponseDTO;
import com.unfv.biblioteca.bibliotecaapi.autenticacion.dto.response.UsuarioDetalleDTO;
import com.unfv.biblioteca.bibliotecaapi.autenticacion.service.AutenticacionService;
import com.unfv.biblioteca.bibliotecaapi.autenticacion.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("git/auth") // URL base para todo lo relacionado con autenticación
@RequiredArgsConstructor
public class AuthController {

    private final AutenticacionService autenticacionService;
    private final UsuarioService usuarioService;

    /**
     * Endpoint para el inicio de sesión de usuarios.
     * Es público.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        AuthResponseDTO response = autenticacionService.login(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para el registro de nuevos usuarios.
     * Es público.
     */
    @PostMapping("/registrar")
    public ResponseEntity<UsuarioDetalleDTO> registrarUsuario(@Valid @RequestBody CrearUsuarioRequestDTO request) {
        UsuarioDetalleDTO usuarioCreado = usuarioService.crearUsuario(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCreado);
    }
}