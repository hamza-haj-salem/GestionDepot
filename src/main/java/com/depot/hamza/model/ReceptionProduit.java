package com.depot.hamza.model;

import lombok.*;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "reception_produit")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "produit")
public class ReceptionProduit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reception_id", nullable = false)
    @JsonBackReference
    private Reception reception;


    @ManyToOne
    @JoinColumn(name = "produit_id", nullable = false)
    @JsonBackReference
    private Produit produit;

    private Integer quantite;

    private Double prixAchat;
    
    
}
