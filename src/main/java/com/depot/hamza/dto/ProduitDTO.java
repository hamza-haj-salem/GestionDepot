package com.depot.hamza.dto;

//ProduitDTO.java
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProduitDTO {
 private Long id;
 private String nom;
 private Double prixAchat;
 private Integer quantite;
}
