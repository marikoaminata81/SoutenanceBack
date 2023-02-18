package com.bezkoder.spring.login.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "panier")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Panier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idpanier;
    private Long quantite;
    private Long totalproduit;
    private boolean etat = true;
    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(cascade = CascadeType.DETACH)
    @JoinTable(
            name = "panier_produits",
            joinColumns = @JoinColumn(name = "panier_id"),
            inverseJoinColumns = @JoinColumn(name = "produit_id")
    )
    private List<Produits> produits = new ArrayList<>();

}
