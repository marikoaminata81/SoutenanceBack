package com.bezkoder.spring.login.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "avertissements")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Avertissement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idavertissemeny;

    private String message;

    @ManyToOne
    @JoinColumn(name = "emetteur_id")
    private User emetteur;

    @ManyToOne
    @JoinColumn(name = "destinataire_id")
    private User destinataire;

    @ManyToOne
    @JoinColumn(name = "video_id")
    private Video video;

    @ManyToOne
    @JoinColumn(name = "commentaire_id")
    private Commentaire commentaire;

    @ManyToOne
    @JoinColumn(name = "produit_id")
    private Produits produits;

    @ManyToOne
    @JoinColumn(name = "plat_id")
    private Plats plats;

}
