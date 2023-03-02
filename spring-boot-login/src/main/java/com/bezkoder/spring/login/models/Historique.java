package com.bezkoder.spring.login.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Historique {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idhistorique;
    private Date Datevente;
    private String nomproduit;
    private Long quantite;
    private  Double prixunitaire;
    private Double montanttotal;
    private  String nomclient;
    private String prenomclient;
    private String numeroclient;

    @ManyToOne
    private User user;


}
