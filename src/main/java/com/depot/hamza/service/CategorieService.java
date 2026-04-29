package com.depot.hamza.service;

import com.depot.hamza.dto.CategorieDTO;
import com.depot.hamza.mapper.CategorieMapper;
import com.depot.hamza.model.Categorie;
import com.depot.hamza.repository.CategorieRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategorieService {

    private final CategorieRepository categorieRepository;

    public CategorieService(CategorieRepository categorieRepository) {
        this.categorieRepository = categorieRepository;
    }

    public Page<CategorieDTO> getAllCategories(Pageable pageable) {
        Page<Categorie> page = categorieRepository.findAll(pageable);
        List<CategorieDTO> dtos = page.getContent().stream()
                .map(CategorieMapper::toDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    public Optional<CategorieDTO> getCategorieById(Long id) {
        return categorieRepository.findById(id).map(CategorieMapper::toDTO);
    }

    public CategorieDTO saveCategorie(CategorieDTO categorieDTO) {
        Categorie categorie = CategorieMapper.toEntity(categorieDTO);
        Categorie saved = categorieRepository.save(categorie);
        return CategorieMapper.toDTO(saved);
    }

    public void deleteCategorie(Long id) {
        categorieRepository.deleteById(id);
    }

    public Optional<CategorieDTO> findByNom(String nom) {
        return categorieRepository.findByNom(nom).map(CategorieMapper::toDTO);
    }
}
