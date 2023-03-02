package com.bezkoder.spring.login.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "panier")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Panier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long quantite;
    private Double totalproduit;
    private boolean etat = true;
    @OneToOne(cascade = CascadeType.DETACH)
    private User user;
    @ManyToMany(cascade = CascadeType.DETACH)
    private List<Produit> produits = new ArrayList<>();


}
