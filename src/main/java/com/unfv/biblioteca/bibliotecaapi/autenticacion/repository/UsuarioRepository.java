package com.unfv.biblioteca.bibliotecaapi.autenticacion.repository;
import org.springframework.stereotype.Repository;
import com.unfv.biblioteca.bibliotecaapi.autenticacion.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    boolean existsByDni(String dni);
    boolean existsByCodigoUniversitario(String codigo);
    boolean existsByEmail(String email);
    Optional<Usuario> findByCodigoUniversitario(String codigo); // Lo usaremos en AuthService

}
