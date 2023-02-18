package com.bezkoder.spring.login.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "produits")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Produits {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idprod;
    private String nomprod;
    private String refprod;
    private String image;
    private String description;
    private String prix_unit;
    private Long nombre;
    private Date datefab;
    private Date dateperem;
    private LocalDate datesoumis;
    private Boolean disponibilite=true;
    private String Statut;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_produits",
            joinColumns = @JoinColumn(name = "produit_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> user = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categorie_id")
    private Categorie categorie ;

}
