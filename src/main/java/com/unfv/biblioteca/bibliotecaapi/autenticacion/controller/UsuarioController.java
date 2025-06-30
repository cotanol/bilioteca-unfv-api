package com.unfv.biblioteca.bibliotecaapi.autenticacion.controller;

import com.unfv.biblioteca.bibliotecaapi.autenticacion.dto.response.UsuarioDetalleDTO;
import com.unfv.biblioteca.bibliotecaapi.autenticacion.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
// import org.springframework.security.core.Authentication; // Se usará con Spring Security real

@RestController
@RequestMapping("/usuarios") // URL base para el recurso Usuario
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    /**
     * Endpoint para obtener los detalles de un usuario por su ID.
     * En un sistema real, este endpoint estaría protegido y solo accesible para Admins.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDetalleDTO> buscarUsuarioPorId(@PathVariable Long id) {
        UsuarioDetalleDTO usuario = usuarioService.buscarUsuarioPorId(id);
        return ResponseEntity.ok(usuario);
    }

    /**
     * Endpoint para que un usuario obtenga sus propios detalles.
     * Es un patrón muy común y seguro. No necesita saber su propio ID.
     */
    @GetMapping("/me")
    public ResponseEntity<UsuarioDetalleDTO> obtenerPerfilPropio() {
        // En un sistema con seguridad real, obtendríamos el ID del token.
        // Authentication authentication -> Inyectado por Spring Security
        // Long usuarioLogueadoId = ((UserDetailsPersonalizado) authentication.getPrincipal()).getId();

        // Para tu demo de mañana, podemos usar un ID fijo para probar.
        Long usuarioLogueadoId = 1L; // <<-- VALOR FIJO SOLO PARA PRUEBAS

        UsuarioDetalleDTO perfil = usuarioService.buscarUsuarioPorId(usuarioLogueadoId);
        return ResponseEntity.ok(perfil);
    }
}