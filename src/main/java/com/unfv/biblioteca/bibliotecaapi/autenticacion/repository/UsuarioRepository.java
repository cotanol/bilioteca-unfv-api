package com.unfv.biblioteca.bibliotecaapi.autenticacion.repository;
import org.springframework.stereotype.Repository;
import com.unfv.biblioteca.bibliotecaapi.autenticacion.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Aquí se puede agregar métodos personalizados si es necesario
    // Por ejemplo, para buscar usuarios por nombre de usuario o correo electrónico

    // public Optional<Usuario> findByUsername(String username);
    // public Optional<Usuario> findByEmail(String email);

    boolean existsByDni(String dni);
    boolean existsByCodigoUniversitario(String codigo);
    boolean existsByEmail(String email);
    Optional<Usuario> findByCodigoUniversitario(String codigo); // Lo usaremos en AuthService

}
