package com.depot.hamza.model;

import lombok.*;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reception")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "receptionProduits") 
public class Reception {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dateReception;

    @ManyToOne
    @JoinColumn(name = "fournisseur_id", nullable = false)
    private Fournisseur fournisseur;

    @OneToMany(mappedBy = "reception", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ReceptionProduit> receptionProduits = new ArrayList<>();

    public void setReceptionProduits(List<ReceptionProduit> newList) {
        this.receptionProduits.clear();
        if (newList != null) {
            this.receptionProduits.addAll(newList);
        }
    }



}
