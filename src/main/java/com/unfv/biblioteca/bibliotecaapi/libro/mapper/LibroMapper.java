package com.unfv.biblioteca.bibliotecaapi.libro.mapper;

import com.unfv.biblioteca.bibliotecaapi.libro.Libro;
import com.unfv.biblioteca.bibliotecaapi.libro.dto.CrearLibroDTO;
import com.unfv.biblioteca.bibliotecaapi.libro.dto.LibroDTO;
import com.unfv.biblioteca.bibliotecaapi.libro.dto.ActualizarLibroDTO;
import org.springframework.stereotype.Component;

@Component
public class LibroMapper {
    public Libro toEntity(CrearLibroDTO dto) {
        Libro libro = new Libro();
        libro.setTitulo(dto.getTitulo());
        libro.setAutor(dto.getAutor());
        return libro;
    }

    public LibroDTO toDto(Libro libro) {
        LibroDTO dto = new LibroDTO();
        dto.setId(libro.getId());
        dto.setTitulo(libro.getTitulo());
        dto.setAutor(libro.getAutor());
        return dto;
    }

    public void updateEntity(ActualizarLibroDTO dto, Libro libro) {
        libro.setTitulo(dto.getTitulo());
        libro.setAutor(dto.getAutor());
    }
}
