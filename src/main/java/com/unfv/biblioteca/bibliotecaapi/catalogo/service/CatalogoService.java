package com.unfv.biblioteca.bibliotecaapi.catalogo.service;

import com.unfv.biblioteca.bibliotecaapi.catalogo.domain.Autor;
import com.unfv.biblioteca.bibliotecaapi.catalogo.domain.Categoria;
import com.unfv.biblioteca.bibliotecaapi.catalogo.domain.Editorial;
import com.unfv.biblioteca.bibliotecaapi.catalogo.domain.Material;
import com.unfv.biblioteca.bibliotecaapi.catalogo.dto.request.CrearMaterialRequestDTO;
import com.unfv.biblioteca.bibliotecaapi.catalogo.dto.response.MaterialDetalleDTO;
import com.unfv.biblioteca.bibliotecaapi.catalogo.mapper.CatalogoMapper;
import com.unfv.biblioteca.bibliotecaapi.catalogo.repository.AutorRepository;
import com.unfv.biblioteca.bibliotecaapi.catalogo.repository.CategoriaRepository;
import com.unfv.biblioteca.bibliotecaapi.catalogo.repository.EditorialRepository;
import com.unfv.biblioteca.bibliotecaapi.catalogo.repository.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class CatalogoService {

    // Declaramos las dependencias como finales (inmutables)
    private final MaterialRepository materialRepository;
    private final AutorRepository autorRepository;
    private final CategoriaRepository categoriaRepository;
    private final EditorialRepository editorialRepository;
    private final CatalogoMapper catalogoMapper;

    // Constructor para inyectar las dependencias
    @Autowired
    public CatalogoService(MaterialRepository materialRepository,
                           AutorRepository autorRepository,
                           CategoriaRepository categoriaRepository,
                           EditorialRepository editorialRepository,
                           CatalogoMapper catalogoMapper) {
        this.materialRepository = materialRepository;
        this.autorRepository = autorRepository;
        this.categoriaRepository = categoriaRepository;
        this.editorialRepository = editorialRepository;
        this.catalogoMapper = catalogoMapper;
    }

    // Metodos para logica de negocio del catálogo de materiales
    // Por ejemplo, buscar materiales, autores, categorías, editoriales, etc.

    @Transactional(readOnly = true)
    public MaterialDetalleDTO buscarMaterialPorId(Long id) {
        Material materialEntidad = materialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Material con ID " + id + " no encontrado"));

        // Usamos el mapper para convertir la entidad a DTO
        return catalogoMapper.toMaterialDetalleDTO(materialEntidad);
    }

    @Transactional
    public MaterialDetalleDTO crearMaterial(CrearMaterialRequestDTO request) {
        if (materialRepository.existsByIsbn(request.getIsbn())) {
            throw new RuntimeException("El ISBN " + request.getIsbn() + " ya existe.");
        }
        // 1. Buscamos las entidades relacionadas usando los IDs del DTO
        Editorial editorial = editorialRepository.findById(request.getEditorialId())
                .orElseThrow(() -> new RuntimeException("Editorial no encontrada"));

        Set<Autor> autores = new HashSet<>(autorRepository.findAllById(request.getAutoresIds()));
        Set<Categoria> categorias = new HashSet<>(categoriaRepository.findAllById(request.getCategoriasIds()));

        // 2. El Mapper convierte los datos simples del DTO a la entidad
        Material nuevoMaterial = catalogoMapper.toMaterial(request);

        // 3. "Enriquecemos" la entidad con las relaciones que ya buscamos
        nuevoMaterial.setEditorial(editorial);
        nuevoMaterial.setAutores(autores);
        nuevoMaterial.setCategorias(categorias);

        // 4. Guardamos la entidad completa en la base de datos
        Material materialGuardado = materialRepository.save(nuevoMaterial);

        // 5. Usamos el mapper de nuevo para devolver el DTO de respuesta con el ID generado
        return catalogoMapper.toMaterialDetalleDTO(materialGuardado);
    }
}
