package com.unfv.biblioteca.bibliotecaapi.libro;

import com.unfv.biblioteca.bibliotecaapi.libro.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/libros")
@RequiredArgsConstructor
public class LibroController {

    private final LibroService libroService;

    @GetMapping
    public ResponseEntity<List<LibroDTO>> obtenerTodos() {
        return ResponseEntity.ok(libroService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LibroDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(libroService.findById(id));
    }

    @PostMapping
    public ResponseEntity<LibroDTO> crear(@Valid @RequestBody CrearLibroDTO crearLibroDTO) {
        return new ResponseEntity<>(libroService.create(crearLibroDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LibroDTO> actualizar(@PathVariable Long id, @Valid @RequestBody ActualizarLibroDTO actualizarLibroDTO) {
        return ResponseEntity.ok(libroService.update(id, actualizarLibroDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        libroService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
