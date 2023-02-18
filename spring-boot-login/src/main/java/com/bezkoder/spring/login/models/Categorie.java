package com.bezkoder.spring.login.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categorie")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Categorie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idcat;
    private String nomcat;
    private String image;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user ;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Produits> produits = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Plats> plats = new HashSet<>();
}
