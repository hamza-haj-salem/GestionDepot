package com.depot.hamza.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReceptionProduitCreationDTO {
    private Long produitId;
    private Integer quantite;
    private Double prixAchat;
    private Boolean isNew = false;  // false par défaut
    private String nomProduit;  // <-- ajoute cette propriété
    private CategorieDTO categorie;  // <-- Ajouté ici
}
