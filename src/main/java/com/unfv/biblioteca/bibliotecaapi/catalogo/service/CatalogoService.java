package com.unfv.biblioteca.bibliotecaapi.catalogo.service;

import com.unfv.biblioteca.bibliotecaapi.catalogo.domain.Autor;
import com.unfv.biblioteca.bibliotecaapi.catalogo.domain.Categoria;
import com.unfv.biblioteca.bibliotecaapi.catalogo.domain.Editorial;
import com.unfv.biblioteca.bibliotecaapi.catalogo.domain.Ejemplar;
import com.unfv.biblioteca.bibliotecaapi.catalogo.domain.Ubicacion;
import com.unfv.biblioteca.bibliotecaapi.catalogo.domain.Material;
import com.unfv.biblioteca.bibliotecaapi.catalogo.dto.request.*;
import com.unfv.biblioteca.bibliotecaapi.catalogo.dto.response.AutorResponseDTO;
import com.unfv.biblioteca.bibliotecaapi.catalogo.dto.response.CategoriaResponseDTO;
import com.unfv.biblioteca.bibliotecaapi.catalogo.dto.response.EditorialResponseDTO;
import com.unfv.biblioteca.bibliotecaapi.catalogo.dto.response.EjemplarResponseDTO;
import com.unfv.biblioteca.bibliotecaapi.catalogo.dto.response.UbicacionResponseDTO;
import com.unfv.biblioteca.bibliotecaapi.catalogo.dto.response.MaterialDetalleDTO;
import com.unfv.biblioteca.bibliotecaapi.catalogo.mapper.CatalogoMapper;
import com.unfv.biblioteca.bibliotecaapi.catalogo.repository.AutorRepository;
import com.unfv.biblioteca.bibliotecaapi.catalogo.repository.CategoriaRepository;
import com.unfv.biblioteca.bibliotecaapi.catalogo.repository.EditorialRepository;
import com.unfv.biblioteca.bibliotecaapi.catalogo.repository.EjemplarRepository;
import com.unfv.biblioteca.bibliotecaapi.catalogo.repository.UbicacionRepository;
import com.unfv.biblioteca.bibliotecaapi.catalogo.repository.MaterialRepository;
import com.unfv.biblioteca.bibliotecaapi.shared.exception.BusinessRuleException;
import com.unfv.biblioteca.bibliotecaapi.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CatalogoService {

    private final MaterialRepository materialRepository;
    private final AutorRepository autorRepository;
    private final CategoriaRepository categoriaRepository;
    private final EditorialRepository editorialRepository;
    private final UbicacionRepository ubicacionRepository;
    private final EjemplarRepository ejemplarRepository;
    private final CatalogoMapper catalogoMapper;

    // =================================================================
    // MÉTODOS PARA MATERIAL
    // =================================================================

    @Transactional(readOnly = true)
    public MaterialDetalleDTO buscarMaterialPorId(Long id) {
        Material materialEntidad = materialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Material con ID " + id + " no encontrado"));
        return catalogoMapper.toMaterialDetalleDTO(materialEntidad);
    }

    @Transactional
    public MaterialDetalleDTO crearMaterial(CrearMaterialRequestDTO request) {
        if (request.getIsbn() != null && materialRepository.existsByIsbn(request.getIsbn())) {
            throw new BusinessRuleException("El ISBN " + request.getIsbn() + " ya existe.");
        }
        Editorial editorial = editorialRepository.findById(request.getEditorialId())
                .orElseThrow(() -> new ResourceNotFoundException("Editorial no encontrada con ID: " + request.getEditorialId()));

        Set<Autor> autores = new HashSet<>(autorRepository.findAllById(request.getAutoresIds()));
        Set<Categoria> categorias = new HashSet<>(categoriaRepository.findAllById(request.getCategoriasIds()));

        Material nuevoMaterial = catalogoMapper.toMaterial(request);
        nuevoMaterial.setEditorial(editorial);
        nuevoMaterial.setAutores(autores);
        nuevoMaterial.setCategorias(categorias);

        Material materialGuardado = materialRepository.save(nuevoMaterial);
        return catalogoMapper.toMaterialDetalleDTO(materialGuardado);
    }

    @Transactional
    public MaterialDetalleDTO actualizarMaterial(Long id, ActualizarMaterialRequestDTO request) {
        Material materialExistente = materialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Material no encontrado con ID: " + id));

        Editorial editorial = editorialRepository.findById(request.getEditorialId())
                .orElseThrow(() -> new ResourceNotFoundException("Editorial no encontrada con ID: " + request.getEditorialId()));

        Set<Autor> autores = new HashSet<>(autorRepository.findAllById(request.getAutoresIds()));
        Set<Categoria> categorias = new HashSet<>(categoriaRepository.findAllById(request.getCategoriasIds()));

        catalogoMapper.updateMaterialFromDto(request, materialExistente);
        materialExistente.setEditorial(editorial);
        materialExistente.setAutores(autores);
        materialExistente.setCategorias(categorias);

        Material materialActualizado = materialRepository.save(materialExistente);
        return catalogoMapper.toMaterialDetalleDTO(materialActualizado);
    }

    @Transactional
    public void eliminarMaterial(Long id) {
        if (!materialRepository.existsById(id)) {
            throw new ResourceNotFoundException("Material no encontrado con ID: " + id);
        }
        // Aquí se podrían agregar reglas de negocio, como no poder eliminar si tiene ejemplares asociados.
        materialRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<MaterialDetalleDTO> findAllMateriales() {
        return materialRepository.findAll().stream()
                .map(catalogoMapper::toMaterialDetalleDTO)
                .collect(Collectors.toList());
    }

    // =================================================================
    // MÉTODOS PARA AUTOR
    // =================================================================

    @Transactional(readOnly = true)
    public List<AutorResponseDTO> findAllAutores() {
        return autorRepository.findAll().stream()
                .map(catalogoMapper::toAutorResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AutorResponseDTO findAutorById(Long id) {
        Autor autor = autorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Autor no encontrado con ID: " + id));
        return catalogoMapper.toAutorResponseDTO(autor);
    }

    @Transactional
    public AutorResponseDTO crearAutor(CrearAutorRequestDTO request) {
        if (autorRepository.existsByNombreCompleto(request.getNombreCompleto())) {
            throw new BusinessRuleException("El autor '" + request.getNombreCompleto() + "' ya existe.");
        }
        Autor nuevoAutor = catalogoMapper.toAutor(request);
        Autor autorGuardado = autorRepository.save(nuevoAutor);
        return catalogoMapper.toAutorResponseDTO(autorGuardado);
    }

    @Transactional
    public AutorResponseDTO actualizarAutor(Long id, ActualizarAutorRequestDTO request) {
        Autor autorExistente = autorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Autor no encontrado con ID: " + id));
        catalogoMapper.updateAutorFromDto(request, autorExistente);
        Autor autorActualizado = autorRepository.save(autorExistente);
        return catalogoMapper.toAutorResponseDTO(autorActualizado);
    }

    @Transactional
    public void eliminarAutor(Long id) {
        if (!autorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Autor no encontrado con ID: " + id);
        }
        if (materialRepository.existsByAutoresId(id)) {
            throw new BusinessRuleException("No se puede eliminar el autor porque está asociado a uno o más materiales.");
        }
        autorRepository.deleteById(id);
    }

    // =================================================================
    // MÉTODOS PARA CATEGORIA
    // =================================================================

    @Transactional(readOnly = true)
    public List<CategoriaResponseDTO> findAllCategorias() {
        return categoriaRepository.findAll().stream()
                .map(catalogoMapper::toCategoriaResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoriaResponseDTO findCategoriaById(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + id));
        return catalogoMapper.toCategoriaResponseDTO(categoria);
    }

    @Transactional
    public CategoriaResponseDTO crearCategoria(CrearCategoriaRequestDTO request) {
        if (categoriaRepository.existsByNombreCategoria(request.getNombreCategoria())) {
            throw new BusinessRuleException("La categoría '" + request.getNombreCategoria() + "' ya existe.");
        }
        Categoria nuevaCategoria = catalogoMapper.toCategoria(request);
        Categoria categoriaGuardada = categoriaRepository.save(nuevaCategoria);
        return catalogoMapper.toCategoriaResponseDTO(categoriaGuardada);
    }

    @Transactional
    public CategoriaResponseDTO actualizarCategoria(Long id, ActualizarCategoriaRequestDTO request) {
        Categoria categoriaExistente = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + id));
        catalogoMapper.updateCategoriaFromDto(request, categoriaExistente);
        Categoria categoriaActualizada = categoriaRepository.save(categoriaExistente);
        return catalogoMapper.toCategoriaResponseDTO(categoriaActualizada);
    }

    @Transactional
    public void eliminarCategoria(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Categoría no encontrada con ID: " + id);
        }
        if (materialRepository.existsByCategoriasId(id)) {
            throw new BusinessRuleException("No se puede eliminar la categoría porque está asociada a uno o más materiales.");
        }
        categoriaRepository.deleteById(id);
    }

    // =================================================================
    // MÉTODOS PARA EDITORIAL
    // =================================================================

    @Transactional(readOnly = true)
    public List<EditorialResponseDTO> findAllEditoriales() {
        return editorialRepository.findAll().stream()
                .map(catalogoMapper::toEditorialResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EditorialResponseDTO findEditorialById(Long id) {
        Editorial editorial = editorialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Editorial no encontrada con ID: " + id));
        return catalogoMapper.toEditorialResponseDTO(editorial);
    }

    @Transactional
    public EditorialResponseDTO crearEditorial(CrearEditorialRequestDTO request) {
        if (editorialRepository.existsByNombre(request.getNombre())) {
            throw new BusinessRuleException("La editorial '" + request.getNombre() + "' ya existe.");
        }
        Editorial nuevaEditorial = catalogoMapper.toEditorial(request);
        Editorial editorialGuardada = editorialRepository.save(nuevaEditorial);
        return catalogoMapper.toEditorialResponseDTO(editorialGuardada);
    }

    @Transactional
    public EditorialResponseDTO actualizarEditorial(Long id, ActualizarEditorialRequestDTO request) {
        Editorial editorialExistente = editorialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Editorial no encontrada con ID: " + id));
        catalogoMapper.updateEditorialFromDto(request, editorialExistente);
        Editorial editorialActualizada = editorialRepository.save(editorialExistente);
        return catalogoMapper.toEditorialResponseDTO(editorialActualizada);
    }

    @Transactional
    public void eliminarEditorial(Long id) {
        if (!editorialRepository.existsById(id)) {
            throw new ResourceNotFoundException("Editorial no encontrada con ID: " + id);
        }
        if (materialRepository.existsByEditorialId(id)) {
            throw new BusinessRuleException("No se puede eliminar la editorial porque está asociada a uno o más materiales.");
        }
        editorialRepository.deleteById(id);
    }

    // =================================================================
    // MÉTODOS PARA UBICACION
    // =================================================================

    @Transactional(readOnly = true)
    public List<UbicacionResponseDTO> findAllUbicaciones() {
        return ubicacionRepository.findAll().stream()
                .map(catalogoMapper::toUbicacionResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UbicacionResponseDTO findUbicacionById(Long id) {
        Ubicacion ubicacion = ubicacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ubicación no encontrada con ID: " + id));
        return catalogoMapper.toUbicacionResponseDTO(ubicacion);
    }

    @Transactional
    public UbicacionResponseDTO crearUbicacion(CrearUbicacionRequestDTO request) {
        Ubicacion nuevaUbicacion = catalogoMapper.toUbicacion(request);
        Ubicacion ubicacionGuardada = ubicacionRepository.save(nuevaUbicacion);
        return catalogoMapper.toUbicacionResponseDTO(ubicacionGuardada);
    }

    @Transactional
    public UbicacionResponseDTO actualizarUbicacion(Long id, ActualizarUbicacionRequestDTO request) {
        Ubicacion ubicacionExistente = ubicacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ubicación no encontrada con ID: " + id));
        catalogoMapper.updateUbicacionFromDto(request, ubicacionExistente);
        Ubicacion ubicacionActualizada = ubicacionRepository.save(ubicacionExistente);
        return catalogoMapper.toUbicacionResponseDTO(ubicacionActualizada);
    }

    @Transactional
    public void eliminarUbicacion(Long id) {
        if (!ubicacionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Ubicación no encontrada con ID: " + id);
        }
        if (ejemplarRepository.existsByUbicacionId(id)) {
            throw new BusinessRuleException("No se puede eliminar la ubicación porque está asociada a uno o más ejemplares.");
        }
        ubicacionRepository.deleteById(id);
    }

    // =================================================================
    // MÉTODOS PARA EJEMPLAR
    // =================================================================

    @Transactional(readOnly = true)
    public List<EjemplarResponseDTO> findAllEjemplares() {
        return ejemplarRepository.findAll().stream()
                .map(catalogoMapper::toEjemplarResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EjemplarResponseDTO findEjemplarById(Long id) {
        Ejemplar ejemplar = ejemplarRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ejemplar no encontrado con ID: " + id));
        return catalogoMapper.toEjemplarResponseDTO(ejemplar);
    }

    @Transactional
    public EjemplarResponseDTO crearEjemplar(CrearEjemplarRequestDTO request) {
        if (ejemplarRepository.existsByCodigoBarras(request.getCodigoBarras())) {
            throw new BusinessRuleException("El código de barras '" + request.getCodigoBarras() + "' ya existe.");
        }
        Material material = materialRepository.findById(request.getMaterialId())
                .orElseThrow(() -> new ResourceNotFoundException("Material no encontrado con ID: " + request.getMaterialId()));
        Ubicacion ubicacion = ubicacionRepository.findById(request.getUbicacionId())
                .orElseThrow(() -> new ResourceNotFoundException("Ubicación no encontrada con ID: " + request.getUbicacionId()));

        Ejemplar nuevoEjemplar = catalogoMapper.toEjemplar(request);
        nuevoEjemplar.setMaterial(material);
        nuevoEjemplar.setUbicacion(ubicacion);

        Ejemplar ejemplarGuardado = ejemplarRepository.save(nuevoEjemplar);
        return catalogoMapper.toEjemplarResponseDTO(ejemplarGuardado);
    }

    @Transactional
    public EjemplarResponseDTO actualizarEjemplar(Long id, ActualizarEjemplarRequestDTO request) {
        Ejemplar ejemplarExistente = ejemplarRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ejemplar no encontrado con ID: " + id));
        Ubicacion ubicacion = ubicacionRepository.findById(request.getUbicacionId())
                .orElseThrow(() -> new ResourceNotFoundException("Ubicación no encontrada con ID: " + request.getUbicacionId()));

        catalogoMapper.updateEjemplarFromDto(request, ejemplarExistente);
        ejemplarExistente.setUbicacion(ubicacion);

        Ejemplar ejemplarActualizado = ejemplarRepository.save(ejemplarExistente);
        return catalogoMapper.toEjemplarResponseDTO(ejemplarActualizado);
    }

    @Transactional
    public void eliminarEjemplar(Long id) {
        if (!ejemplarRepository.existsById(id)) {
            throw new ResourceNotFoundException("Ejemplar no encontrado con ID: " + id);
        }
        // Aquí se podrían agregar reglas de negocio, como no poder eliminar si está en préstamo.
        ejemplarRepository.deleteById(id);
    }
}
