package com.depot.hamza.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReceptionDetailsDTO {
 private Long id;
 private LocalDateTime dateReception;
 private FournisseurDTO fournisseur;
 private List<ReceptionProduitDetailsDTO> receptionProduits;
}

