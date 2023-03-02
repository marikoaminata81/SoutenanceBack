package com.bezkoder.spring.login.controllers;

import com.bezkoder.spring.login.models.Panier;
import com.bezkoder.spring.login.models.Produit;
import com.bezkoder.spring.login.models.User;
import com.bezkoder.spring.login.payload.response.MessageResponse;
import com.bezkoder.spring.login.repository.PanierRepository;
import com.bezkoder.spring.login.repository.ProduitRepository;
import com.bezkoder.spring.login.repository.UserRepository;
import com.bezkoder.spring.login.security.services.PanierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping(path = "/panier")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PanierContoller {
    @Autowired
    private PanierService panierService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PanierRepository panierRepository;
    @Autowired
    private ProduitRepository produitRepository;

    //   ICI ON AJOUTE UN PRODUIT AU PANIER
    @PostMapping("/ajouter/{produit}/{user}")
    public MessageResponse Ajouter(@RequestBody Panier panier,
                                   @PathVariable("produit") Produit produit,
                                   @PathVariable("user") User user) {
        Optional<User> use = userRepository.findById(user.getId());
        List<Panier> panier1 = panierRepository.findByUser(user);
        // List<Panier> paniers = panierRepository.findByUserAndEtat()


        if(use != null) {

            Long Qte =  (panier.getQuantite());
            panier.setUser(user);

            panier.setQuantite(1L);

            panier.setTotalproduit((produit.getPrix()));
            panier.getProduits().add(produit);

            return  panierService.Ajouter(panier,produit,user);
        }else {
            MessageResponse message = new MessageResponse("Impossible d'ajouté au panier",false);
            return message;
        }

    }   //   ICI ON AJOUTE UN PRODUIT AU PANIER EN PRECISANT LE NOMBRE DE PRODUITS

    @PostMapping("/modifierquantite/{produit}/{user}")
    public MessageResponse AjouterModifier(@RequestBody Panier panier, @PathVariable("produit") Produit produit, @PathVariable("user") User user) {

        Optional<User> use = userRepository.findById(user.getId());
        if(use != null ) {
            Long Qte =  (panier.getQuantite());
            panier.setUser(user);
            panier.setQuantite(panier.getQuantite());
            panier.setTotalproduit((produit.getPrix()));
            panier.getProduits().add(produit);
            return  panierService.Modifier(panier,produit,user);
        }else {
            MessageResponse message = new MessageResponse("Impossible d'ajouté au panier",false);
            return message;
        }
    }


    @DeleteMapping("/supprimer/{panier}/{produit}/{user}")
    public MessageResponse Supprimer(@PathVariable("panier") Panier panier, @PathVariable("produit") Produit produit, @PathVariable("user") Long user) {
        if(panierRepository.findById(panier.getId()) != null && userRepository.findById(user) != null && produitRepository.findById(produit.getId()) != null ) {
            return  panierService.Supprimer(panier,produit,user);
        }else {
            MessageResponse message = new MessageResponse("Impossible de supprimer", false);
            return message;
        }

    }






    @GetMapping("/panierUserTotal/{id}")
    public Object detailPanier(@PathVariable("id") Long id){
        return panierRepository.detail(id);

    }

    @GetMapping("/panierParUser/{users}/{etat}")
    public List<Panier> panierParUsers(@PathVariable("users") User user, @PathVariable("etat") boolean etat){
        return panierRepository.findByUserAndEtat(user,etat);

    }




    //@PostMapping("/ajouter/{produit}/{user}")
    @MessageMapping("/getCart")
    @SendTo("/panier/cart")
    public MessageResponse Ajouters(@RequestBody Panier panier, @PathVariable("produit") Produit produit, @PathVariable("user") User user) {

        if(userRepository.findById(user.getId()) != null) {
            Long Qte =  (panier.getQuantite());
            panier.setUser(user);
            panier.setQuantite(1L);
            panier.setTotalproduit((produit.getPrix()));
            panier.getProduits().add(produit);

            return  panierService.Ajouter(panier,produit,user);
        }else {
            MessageResponse message = new MessageResponse("Impossible d'ajouté au panier",false);
            return message;
        }

    }
}
