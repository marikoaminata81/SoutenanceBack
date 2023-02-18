package com.bezkoder.spring.login.security.services;

import com.bezkoder.spring.login.models.Categorie;
import com.bezkoder.spring.login.models.Produits;
import com.bezkoder.spring.login.models.User;
import com.bezkoder.spring.login.payload.response.MessageResponse;
import com.bezkoder.spring.login.repository.ProduitsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProduitsServiceImpl implements ProduitsService {


    @Autowired
     ProduitsRepository produitRepository;
    @Override
    public MessageResponse Ajouter(Produits produit, User user, Categorie categorie) {

        if(produitRepository.findByRefprod(produit.getRefprod()) == null){
            produit.getUser().add(user);
            produit.setCategorie(categorie);

            produitRepository.save(produit);
            MessageResponse message = new MessageResponse("Produit créer avec succès",true);
            return message;
        }
        else{
            MessageResponse message = new MessageResponse("Cette reference de prodits  existe déjà !",false);
            return message;
        }
    }

    @Override
    public MessageResponse Modifier(Produits produit, Long idprod) {
        Optional<Produits> produit1 = produitRepository.findById(idprod);
        if(!produit1.isPresent()){

            MessageResponse message = new MessageResponse("Ce produit n'est pas trouvée !", false);
            return message;
        }
        else {

            Produits produit2 = produitRepository.findById(idprod).get();
            produit2.setNomprod(produit.getNomprod());
            produit2.setRefprod(produit.getRefprod());
            produit2.setImage(produit.getImage());
            produit2.setDescription(produit.getDescription());
            produit2.setPrix_unit(produit.getPrix_unit());
            produit2.setNombre(produit.getNombre());
            produit2.setDatefab(produit.getDatefab());
            produit2.setDateperem(produit.getDateperem());
            produit2.setDisponibilite(produit.getDisponibilite());

            this.produitRepository.save(produit2);

            MessageResponse message = new MessageResponse("Produit modifiée avec succès !", true);
            return message;


        }
    }

    @Override
    public MessageResponse Supprimer(Long idprod) {
        Optional<Produits> produit = this.produitRepository.findById(idprod);
        if (!produit.isPresent())
        {
            MessageResponse message = new MessageResponse("Produit non trouvé !", false);
            return message;
        }
        else {
            this.produitRepository.delete(produit.get());
            MessageResponse message = new MessageResponse("Produit supprimé avec succès !", true);
            return message;
        }
    }

    @Override
    public MessageResponse SetDisponibilite(Produits produit, Long idprod) {
        Optional<Produits> produit1 = produitRepository.findById(idprod);
        if(produit1.isPresent()){
            Produits produit2 = produitRepository.findById(idprod).get();
            produit2.setDisponibilite(produit.getDisponibilite());
            this.produitRepository.save(produit2);
            MessageResponse message = new MessageResponse("Etat modifiée avec succès !", true);
            return message;
        }
        else {
            MessageResponse message = new MessageResponse("Produit non modifiés !e", false);
            return message;


        }
    }

    @Override
    public List<Produits> Lister() {
        return produitRepository.findAll();
    }

    @Override
    public List<Produits> AfficherLesProduitsParDisponibilite(boolean disponibilite) {
        return produitRepository.AfficherLesProduitsParDisponibilite(disponibilite);
    }

    @Override
    public List<Produits> produitsDisponible() {
        return produitRepository.produitsDisponible();
    }

    @Override
    public List<Produits> produitsIndisponible() {
        return produitRepository.produitsIndisponible();
    }

    @Override
    public List<Produits> produitsEnAttente() {
        return produitRepository.produitsEnAttente();
    }

    @Override
    public List<Produits> produitsPoste() {
        return produitRepository.produitsPoste();
    }

    @Override
    public List<Produits> produitsrejet() {
        return produitRepository.produitsrejet();
    }
}
