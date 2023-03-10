package com.bezkoder.spring.login.controllers;

import com.bezkoder.spring.login.models.Commande;
import com.bezkoder.spring.login.models.Panier;
import com.bezkoder.spring.login.models.User;
import com.bezkoder.spring.login.payload.response.MessageResponse;
import com.bezkoder.spring.login.repository.PanierRepository;
import com.bezkoder.spring.login.security.services.CommandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@CrossOrigin
@CrossOrigin(origins ={ "http://localhost:8100/" }, maxAge = 3600, allowCredentials="true")
@RequestMapping(path = "/api/v1/commande")
public class CommandeController {

    @Autowired
    private CommandeService commandeService;
    @Autowired
    private PanierRepository panierRepository;

    @PostMapping("/ajouter/{id}")
    public MessageResponse ajouter(Commande commande, @PathVariable("id") User user){
        List<Panier> panier = panierRepository.findByUser(user);
        return commandeService.ajouter(commande,user);
    }

}
