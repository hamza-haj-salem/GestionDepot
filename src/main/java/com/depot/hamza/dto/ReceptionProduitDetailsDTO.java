package com.depot.hamza.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReceptionProduitDetailsDTO {
 private Long id;
 private ProduitDTO produit;
 private Integer quantite;
 private Double prixAchat;
}

