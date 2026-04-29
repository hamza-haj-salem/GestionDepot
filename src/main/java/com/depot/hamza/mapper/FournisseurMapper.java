package com.depot.hamza.mapper;

import com.depot.hamza.dto.FournisseurDTO;
import com.depot.hamza.model.Fournisseur;
import org.springframework.stereotype.Component;

@Component
public class FournisseurMapper {

    public FournisseurDTO toDTO(Fournisseur fournisseur) {
        if (fournisseur == null) {
            return null;
        }
        return new FournisseurDTO(
                fournisseur.getId(),
                fournisseur.getNom()
        );
    }

    public Fournisseur toEntity(FournisseurDTO dto) {
        if (dto == null) {
            return null;
        }
        Fournisseur fournisseur = new Fournisseur();
        fournisseur.setId(dto.getId());
        fournisseur.setNom(dto.getNom());
        return fournisseur;
    }
}
