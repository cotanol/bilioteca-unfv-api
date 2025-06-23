package com.unfv.biblioteca.bibliotecaapi.libro;

import com.unfv.biblioteca.bibliotecaapi.libro.dto.*;
import com.unfv.biblioteca.bibliotecaapi.libro.mapper.LibroMapper;
import com.unfv.biblioteca.bibliotecaapi.shared.exception.ResourceNotFoundException;
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
        // 1. Buscar el libro existente
        Libro libroActual = libroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado con id: " + id));

        // 2. Lógica de actualización parcial explícita
        // Comprobar y actualizar el título si se proporcionó uno nuevo (Verificar los nulls)
        if (actualizarLibroDTO.getTitulo() != null && !actualizarLibroDTO.getTitulo().isBlank()) {
            libroActual.setTitulo(actualizarLibroDTO.getTitulo());
        }

        // Comprobar y actualizar el autor si se proporcionó uno nuevo
        if (actualizarLibroDTO.getAutor() != null && !actualizarLibroDTO.getAutor().isBlank()) {
            libroActual.setAutor(actualizarLibroDTO.getAutor());
        }

        // 3. Guardar la entidad actualizada
        Libro libroGuardado = libroRepository.save(libroActual);

        // 4. Mapear la entidad final a DTO para la respuesta
        return libroMapper.toDto(libroGuardado);
    }

    @Override
    public void deleteById(Long id) {
        if (!libroRepository.existsById(id)) {
            throw new RuntimeException("Libro no encontrado con id: " + id);
        }
        libroRepository.deleteById(id);
    }
}
