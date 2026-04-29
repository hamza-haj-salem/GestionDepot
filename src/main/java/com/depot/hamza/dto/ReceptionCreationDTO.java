package com.depot.hamza.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReceptionCreationDTO {
    private Long fournisseurId;
    private List<ReceptionProduitCreationDTO> produits;
}
