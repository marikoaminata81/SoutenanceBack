package com.bezkoder.spring.login.security.services;

import com.bezkoder.spring.login.models.Categorie;
import com.bezkoder.spring.login.models.User;
import com.bezkoder.spring.login.payload.response.MessageResponse;

import java.util.List;

public interface CategorieService {
    MessageResponse Ajouter(Categorie categorie, User user);

    MessageResponse Modifier(Categorie categorie, Long idcat );

    MessageResponse Supprimer(Long idcat);

    List<Categorie> Lister();
}
