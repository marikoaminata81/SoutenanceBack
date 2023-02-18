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
@Table(name = "plats")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Plats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idplat;
    private String nomplat;
    private String image;
    private String prix_unit;
    private LocalDate datesoumis;
    private Long nombre;
    private Boolean disponibilite=true;
    private String Statut;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_plat",
            joinColumns = @JoinColumn(name = "plat_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> user = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categorie_id")
    private Categorie categorie;

}
