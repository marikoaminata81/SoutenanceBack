package com.bezkoder.spring.login.security.services;

import com.bezkoder.spring.login.models.Commande;
import com.bezkoder.spring.login.models.User;
import com.bezkoder.spring.login.payload.response.MessageResponse;

public interface CommandeService {

     MessageResponse ajouter(Commande commande, User user);

}
