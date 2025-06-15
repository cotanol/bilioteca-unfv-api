package com.unfv.biblioteca.bibliotecaapi.libro;

import com.unfv.biblioteca.bibliotecaapi.libro.dto.*;
import com.unfv.biblioteca.bibliotecaapi.libro.mapper.LibroMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LibroServiceImpl implements LibroService{

    private final LibroRepository libroRepository;
    private final LibroMapper libroMapper;

    @Override
    public List<LibroDTO> findAll() {
        return libroRepository.findAll().stream()
                .map(libroMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public LibroDTO findById(Long id) {
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado con id: " + id)); // Excepción simple por ahora
        return libroMapper.toDto(libro);
    }

    @Override
    public LibroDTO create(CrearLibroDTO crearLibroDTO) {
        Libro libro = libroMapper.toEntity(crearLibroDTO);
        return libroMapper.toDto(libroRepository.save(libro));
    }

    @Override
    public LibroDTO update(Long id, ActualizarLibroDTO actualizarLibroDTO) {
        Libro libroActual = libroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado con id: " + id));

        libroMapper.updateEntity(actualizarLibroDTO, libroActual);

        return libroMapper.toDto(libroRepository.save(libroActual));
    }

    @Override
    public void deleteById(Long id) {
        if (!libroRepository.existsById(id)) {
            throw new RuntimeException("Libro no encontrado con id: " + id);
        }
        libroRepository.deleteById(id);
    }
}
