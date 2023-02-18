package com.bezkoder.spring.login.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "commande")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Commande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idcommande;
    private LocalDate datecommande;
    private String refcommande;
    private Long quantitecommande;
    private Long montanttotal;
    private String statut;


    @ManyToOne(cascade = CascadeType.DETACH)
    private User user;

}
