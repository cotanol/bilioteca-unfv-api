package com.unfv.biblioteca.bibliotecaapi.autenticacion.repository;

import com.unfv.biblioteca.bibliotecaapi.autenticacion.domain.TipoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoUsuarioRepository extends JpaRepository<TipoUsuario, Long> {

    boolean existsByNombreTipo(String nombreTipo);
}