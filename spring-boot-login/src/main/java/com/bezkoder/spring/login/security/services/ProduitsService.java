package com.bezkoder.spring.login.security.services;

import com.bezkoder.spring.login.models.Categorie;
import com.bezkoder.spring.login.models.Produits;
import com.bezkoder.spring.login.models.User;
import com.bezkoder.spring.login.payload.response.MessageResponse;

import java.util.List;

public interface ProduitsService {

    MessageResponse Ajouter(Produits produit, User user, Categorie categorie);

    MessageResponse Modifier(Produits produit, Long iduser);

    MessageResponse Supprimer(Long idprod);

    MessageResponse SetDisponibilite(Produits produit, Long iduser);

    List<Produits> Lister();

    public List<Produits> AfficherLesProduitsParDisponibilite(boolean disponibilite);
    List<Produits> produitsDisponible();
    List<Produits> produitsIndisponible();
    List<Produits> produitsEnAttente();
    List<Produits> produitsPoste();
    List<Produits> produitsrejet();

}
