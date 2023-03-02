package com.bezkoder.spring.login.security.services;

import com.bezkoder.spring.login.models.Panier;
import com.bezkoder.spring.login.models.Produit;
import com.bezkoder.spring.login.models.User;
import com.bezkoder.spring.login.payload.response.MessageResponse;

public interface PanierService {
    MessageResponse Ajouter(Panier panier, Produit produit, User user);

    MessageResponse Modifier(Panier panier, Produit produit, User user);
    MessageResponse Supprimer(Panier panier, Produit produit, Long user);
}
