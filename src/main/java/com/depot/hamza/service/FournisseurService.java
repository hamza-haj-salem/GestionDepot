package com.depot.hamza.service;

import com.depot.hamza.dto.FournisseurDTO;
import com.depot.hamza.mapper.FournisseurMapper;
import com.depot.hamza.model.Fournisseur;
import com.depot.hamza.repository.FournisseurRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FournisseurService {

    private final FournisseurRepository fournisseurRepository;
    private final FournisseurMapper fournisseurMapper;

    public FournisseurService(FournisseurRepository fournisseurRepository,
                               FournisseurMapper fournisseurMapper) {
        this.fournisseurRepository = fournisseurRepository;
        this.fournisseurMapper = fournisseurMapper;
    }

    public Page<FournisseurDTO> getAllFournisseurs(Pageable pageable) {
        return fournisseurRepository.findAll(pageable)
                .map(fournisseurMapper::toDTO);
    }

    public Optional<Fournisseur> getFournisseurById(Long id) {
        return fournisseurRepository.findById(id);
    }

    public Fournisseur saveFournisseur(Fournisseur fournisseur) {
        return fournisseurRepository.save(fournisseur);
    }

    public void deleteFournisseur(Long id) {
        fournisseurRepository.deleteById(id);
    }
}
