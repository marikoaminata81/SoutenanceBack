package com.bezkoder.spring.login.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Commande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idcommande;
    private LocalDate datecommande;
    private String codecommande;
    private Long quantitecommande;
    private Double montanttotal;
    private String status;
/*
    @OneToOne(cascade = CascadeType.DETACH)
    private Panier paniers ;
 */

    @ManyToOne(cascade = CascadeType.DETACH)
    private User user;

}
