package com.bezkoder.spring.login.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "produit")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Produit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String reference;
    private String imagecouverture;
    private String description;
    private Double prix;
   // private Long quantiteVente;
    private boolean etat;
    @ManyToOne
    @JoinColumn(name = "categorie_id")
    private Categorie categorie;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Produit produit = (Produit) o;
        return Objects.equals(id, produit.id) && Objects.equals(author, produit.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, author);
    }
}
