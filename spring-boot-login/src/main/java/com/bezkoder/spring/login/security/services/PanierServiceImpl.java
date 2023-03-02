package com.bezkoder.spring.login.security.services;

import com.bezkoder.spring.login.models.Panier;
import com.bezkoder.spring.login.models.Produit;
import com.bezkoder.spring.login.models.User;
import com.bezkoder.spring.login.payload.response.MessageResponse;
import com.bezkoder.spring.login.repository.PanierRepository;
import com.bezkoder.spring.login.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PanierServiceImpl implements PanierService {
    @Autowired
    private PanierRepository panierRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Override
    public MessageResponse Ajouter(Panier panier, Produit produit, User user) {
        Boolean panier4 = panierRepository.existsByProduitsAndUserAndEtat(produit,user,true);
        Panier pa = panierRepository.findByProduitsAndUserAndEtat(produit,user,true);
        User user1 = userService.getAuthenticatedUser();
        if(!panier4 ){
            panier.setUser(user1);
            panier.setQuantite(panier.getQuantite());
            panier.setTotalproduit((produit.getPrix()));
            panier.setEtat(true);
            panierRepository.save(panier);
            MessageResponse message = new MessageResponse("Produit ajouté au panier", true);
            return message;
        }
        else {
            pa.setTotalproduit( (pa.getTotalproduit() + (produit.getPrix()*panier.getQuantite())));
            pa.setQuantite(pa.getQuantite() + panier.getQuantite());
            panierRepository.save(pa);
            MessageResponse message = new MessageResponse("Produit ajouté au panier",true);
            return message;
        }
    }

    @Override
    public MessageResponse Modifier(Panier panier, Produit produit, User user) {
        Boolean panier4 = panierRepository.existsByProduitsAndUserAndEtat(produit,user,true);
        Panier pa = panierRepository.findByProduitsAndUserAndEtat(produit,user,true);
        User user1 = userService.getAuthenticatedUser();
        if(!panier4 ){
            panier.setUser(user);
            panier.setQuantite(panier.getQuantite());
            panier.setTotalproduit((produit.getPrix()));
            panier.setEtat(true);
            panierRepository.save(panier);
            MessageResponse message = new MessageResponse("Produit ajouté au panier", true);
            return message;
        }
        else {
            pa.setTotalproduit((produit.getPrix()*panier.getQuantite()));
            pa.setQuantite(panier.getQuantite());
            panierRepository.save(pa);
            MessageResponse message = new MessageResponse("Produit ajouté au panier",true);
            return message;
        }
    }

    @Override
    public MessageResponse Supprimer(Panier panier, Produit produit, Long user) {

        User user1 = userRepository.findById(user).get();
        Panier pa = panierRepository.findByProduitsAndUserAndEtat(produit,user1,true);

        panierRepository.delete(pa);
        MessageResponse message = new MessageResponse("Produit supprimer du panier",true);
        return message;
    }
}
