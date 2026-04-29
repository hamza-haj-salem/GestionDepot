package com.depot.hamza.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.*;

@Entity
@Table(name = "produit")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    private String description;

    @ManyToOne
    @JoinColumn(name = "categorie_id" , nullable = true)   
    private Categorie categorie;  

    private Double prixAchat;

    private Double prixVente;

    private Integer quantite;  
    
    
    
    /*
    @OneToMany(mappedBy = "produit", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference   
    @JsonIgnore
    private List<ReceptionProduit> receptionProduits = new ArrayList<>();

    public void setReceptionProduits(List<ReceptionProduit> newList) {
        this.receptionProduits.clear();
        if (newList != null) {
            this.receptionProduits.addAll(newList);
        }
    }*/
}
