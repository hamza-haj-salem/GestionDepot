package com.depot.hamza.dto;

import com.depot.hamza.model.Produit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrixConflitResponseDTO {

    private String message;
    private List<Produit> produitsSimilaires;

    
}
