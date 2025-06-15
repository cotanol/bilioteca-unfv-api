package com.unfv.biblioteca.bibliotecaapi.libro;

import com.unfv.biblioteca.bibliotecaapi.libro.dto.*;
import java.util.List;

public interface LibroService {
    List<LibroDTO> findAll();
    LibroDTO findById(Long id);
    LibroDTO create(CrearLibroDTO crearLibroDTO);
    LibroDTO update(Long id, ActualizarLibroDTO actualizarLibroDTO);
    void deleteById(Long id);
}
