package com.unfv.biblioteca.bibliotecaapi.autenticacion.service;

import com.unfv.biblioteca.bibliotecaapi.autenticacion.domain.Usuario;
import com.unfv.biblioteca.bibliotecaapi.autenticacion.dto.request.LoginRequestDTO;
import com.unfv.biblioteca.bibliotecaapi.autenticacion.dto.response.AuthResponseDTO;
import com.unfv.biblioteca.bibliotecaapi.autenticacion.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AutenticacionService {
    private final UsuarioRepository usuarioRepository;

    public AuthResponseDTO login(LoginRequestDTO request) {
        // ¡¡¡ADVERTENCIA DE SEGURIDAD: LÓGICA DE LOGIN SOLO PARA DEMO!!!
        // Este proceso no es seguro y solo debe usarse para pruebas rápidas.
        // El proceso real usa Spring Security, AuthenticationManager y PasswordEncoder.

        // 1. Buscamos al usuario por su código
        Usuario usuario = usuarioRepository.findByCodigoUniversitario(request.getCodigoUniversitario())
                .orElseThrow(() -> new RuntimeException("Credenciales incorrectas"));

        // 2. Comparamos la contraseña en texto plano (INSEGURO, SOLO PARA DEMO)
        if (!usuario.getPasswordHash().equals(request.getPassword())) {
            throw new RuntimeException("Credenciales incorrectas");
        }

        // 3. Si las credenciales "coinciden", generamos un token falso
        // En la vida real, aquí se genera un JWT con la información del usuario y una firma.
        String fakeToken = "fake-jwt-token-for-" + usuario.getEmail();

        return AuthResponseDTO.builder()
                .token(fakeToken)
                .build();
    }

}
