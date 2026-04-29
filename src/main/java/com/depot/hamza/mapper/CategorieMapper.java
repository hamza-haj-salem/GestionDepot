package com.depot.hamza.mapper;

import com.depot.hamza.dto.CategorieDTO;
import com.depot.hamza.model.Categorie;

public class CategorieMapper {

    public static CategorieDTO toDTO(Categorie categorie) {
        if (categorie == null) return null;
        return new CategorieDTO(categorie.getId(), categorie.getNom());
    }

    public static Categorie toEntity(CategorieDTO dto) {
        if (dto == null) return null;
        Categorie categorie = new Categorie();
        categorie.setId(dto.getId());
        categorie.setNom(dto.getNom());
        return categorie;
    }
}
