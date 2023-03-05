package com.bezkoder.spring.login.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "plat")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Plats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String reference;
    private String imagecouverture;
    private String description;
    private Double prix;
    private LocalDate datesoumis;
    // private Long quantiteVente;
    private boolean etat;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "categorie_id")
    private Categorie categorie;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Plats plats = (Plats) o;
        return Objects.equals(id, plats.id) && Objects.equals(author, plats.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, author);
    }
}
